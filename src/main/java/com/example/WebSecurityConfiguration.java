package com.example;

import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

@Configuration
public class WebSecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .anyRequest().fullyAuthenticated()
                )
                .formLogin(Customizer.withDefaults());

        return http.build();
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth, BaseLdapPathContextSource contextSource, UserDetailsService userDetailsService) throws Exception {
        // use auth.authenticationProvider(new ActiveDirectoryLdapAuthenticationProvider()) instead for Active Directory
        auth
                .ldapAuthentication()
                .userDnPatterns("uid={0},ou=people")
                .groupSearchBase("ou=groups")
                .contextSource(contextSource)
                .passwordCompare()
                .passwordEncoder(new BCryptPasswordEncoder())
                .passwordAttribute("userPassword")
                .and()
                .userDetailsContextMapper(new UserDetailsContextMapper() {
                    @Override
                    public UserDetails mapUserFromContext(DirContextOperations ctx, String username, Collection<? extends GrantedAuthority> authorities) {
                        return userDetailsService.loadUserByUsername(username);
                    }

                    @Override
                    public void mapUserToContext(UserDetails user, DirContextAdapter ctx) {

                    }
                });

        auth.userDetailsService(userDetailsService);
    }

    @Bean
    @SuppressWarnings("deprecation")
    public static UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(User.withDefaultPasswordEncoder()
                .username("user")
                .password("secret")
                .roles("USER", "ADMIN")
                .build(), User.withDefaultPasswordEncoder()
                .username("demo")
                .password("password")
                .roles("DEMO")
                .build());
    }
}

