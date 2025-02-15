package dev.valente.picpaysimplificado.apitests;

import com.github.tomakehurst.wiremock.WireMockServer;
import dev.valente.picpaysimplificado.config.RestAssuredConfig;
import dev.valente.picpaysimplificado.config.TestContainers;
import dev.valente.picpaysimplificado.exception.NotAuthorizedException;
import dev.valente.picpaysimplificado.service.AuthorizationService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = RestAssuredConfig.class)
@AutoConfigureWireMock(port = 0, files = "classpath:/wiremock/authorizationapi")
public class AuthorizationApiTestIT extends TestContainers {

    @MockitoSpyBean
    private AuthorizationService authorizationService;

    @Autowired
    private WireMockServer wireMockServer;

    @Test
    @Order(1)
    @DisplayName("the external api should return a json with status success")
    void authorizationApi_ShouldReturnJsonWithStatusSuccess_WhenAuthorized() throws IOException {

        stubFor(get(urlEqualTo("/api/v2/authorize"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("get_authorizedtransactionapi_200.json")
                ));

        authorizationService.getAuthorization();

    }

    @Test
    @Order(2)
    @DisplayName("The external api should return a json with status fail")
    void authorizationApi_ShouldReturnJsonWithStatusFail_WhenTransactionIsNotAuthorized() {

        stubFor(get(urlEqualTo("/api/v2/authorize"))
                .willReturn(aResponse()
                        .withStatus(403)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("get_notauthorizedtransactionapi_403.json")
                ));


        Assertions.assertThatException().isThrownBy(() -> authorizationService.getAuthorization())
                .withMessage("403 FORBIDDEN \"Authorization[status=fail, data=Data[authorization=false]]\"")
                .isInstanceOf(NotAuthorizedException.class);

    }

}
