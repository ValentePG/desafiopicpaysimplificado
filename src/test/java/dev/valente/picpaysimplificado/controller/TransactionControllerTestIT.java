package dev.valente.picpaysimplificado.controller;

import dev.valente.picpaysimplificado.config.RestAssuredConfig;
import dev.valente.picpaysimplificado.config.TestContainers;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = RestAssuredConfig.class)
class TransactionControllerTestIT extends TestContainers {

    public final String URL = "/v1/transfer";

    @Autowired
    private RequestSpecification requestSpecification;

    @BeforeEach
    void setUp() {
        RestAssured.requestSpecification = requestSpecification;
    }

    @Test
    void createTransaction() {
        var response = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .body("")
                .when()
                .post(URL)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .log().all()
                .extract().response().body().asString();

        System.out.println(response);

    }
}