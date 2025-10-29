package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.FormLoginRequestBuilder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MainApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void loginWithValidUserAndUsingLdapPassword() throws Exception {
        FormLoginRequestBuilder login = formLogin()
                .user("user")
                .password("password");

        mockMvc.perform(login)
                .andExpect(authenticated().withUsername("user").withRoles("USER", "ADMIN"));
    }

    @Test
    public void loginWithValidUserAndUsingUdsPassword() throws Exception {
        FormLoginRequestBuilder login = formLogin()
                .user("user")
                .password("secret");

        mockMvc.perform(login)
                .andExpect(authenticated().withUsername("user").withRoles("USER", "ADMIN"));
    }

    @Test
    public void loginWithUserOnlyInUserDetailsService() throws Exception {
        FormLoginRequestBuilder login = formLogin()
                .user("demo")
                .password("password");

        mockMvc.perform(login)
                .andExpect(authenticated().withUsername("demo").withRoles("DEMO"));
    }

    @Test
    public void loginWithInvalidUserThenUnauthenticated() throws Exception {
        FormLoginRequestBuilder login = formLogin()
                .user("user")
                .password("invalidpassword");

        mockMvc.perform(login)
                .andExpect(unauthenticated());
    }
}
