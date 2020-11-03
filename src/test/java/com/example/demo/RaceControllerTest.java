package com.example.demo;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Testcontainers
class RaceControllerTest extends IntegrationTest {

    @Autowired
    private RaceRepository races;

    @Test
    public void dbstuff() {
        Race race = new Race();
        race.setName("test one");
        races.save(race);

        assertThat(races.findAll()).isNotEmpty();
        assertThat(races.findAll()).hasSize(1);
    }

    @Test
    public void dbstuff2() {
        Race race = new Race();
        race.setName("test two");
        races.save(race);

        assertThat(races.findAll()).isNotEmpty();
        assertThat(races.findAll()).hasSize(1);
    }

    @Nested
    @DisplayName("GET /")
    @SpringBootTest
    class IndexRouteTests extends IntegrationTest {

        @Autowired
        private RaceRepository repository;

        @Test
        @DisplayName("The status code of the index route is 200")
        public void index() {
            when()
                .get("/")
            .then()
                .statusCode(200);
        }

        @Test
        @DisplayName("When the database is empty, the returned info is an empty array")
        public void indexEmpty() {
            when()
                .get("/")
            .then()
                .body("$", Matchers.empty());
        }

        @Test
        @DisplayName("When there is one item in the database, we receive it")
        public void indexOne() {
            Race race = new Race();
            race.setName("Foo");
            repository.save(race);

            when()
                .get("/")
            .then()
                .body("$", Matchers.hasSize(1));
        }
    }
}