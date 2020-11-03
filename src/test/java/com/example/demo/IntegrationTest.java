package com.example.demo;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;

abstract class IntegrationTest {
    @Autowired
    private WebApplicationContext webApplicationContext;


    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer<>();

    static {
        postgreSQLContainer.start();
    }

    @BeforeEach
    public void setup(@Autowired Flyway flyway) {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
        flyway.clean();
        flyway.migrate();
    }

    @BeforeAll
    public static void setupAll() {
    }

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    }
}
