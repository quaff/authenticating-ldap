plugins {
    java
	id("org.springframework.boot").version("3.5.7")
    id("io.spring.dependency-management").version("latest.release")
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    version = 21
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.security:spring-security-ldap")
	implementation("com.unboundid:unboundid-ldapsdk") // embedded LDAP server
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
