package dev.valente.picpaysimplificado.config;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

import static dev.valente.picpaysimplificado.utils.Constants.BASE_URI;

@TestConfiguration
@Lazy
public class RestAssuredConfig {

    @LocalServerPort
    int port;

    @Bean
    public RequestSpecification requestSpecification() {
        return RestAssured.given()
                .baseUri(BASE_URI + port);
    }
}
