package dev.valente.picpaysimplificado.apitests;

import com.github.tomakehurst.wiremock.WireMockServer;
import dev.valente.picpaysimplificado.config.RestAssuredConfig;
import dev.valente.picpaysimplificado.config.TestContainers;
import dev.valente.picpaysimplificado.domain.Transaction;
import dev.valente.picpaysimplificado.service.NotifyClient;
import dev.valente.picpaysimplificado.service.NotifyConsumer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static dev.valente.picpaysimplificado.utils.TransactionUtils.AFTER_TRANSACTION_SUCCESS;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = RestAssuredConfig.class)
@AutoConfigureWireMock(port = 0, files = "classpath:wiremock/notificationapi")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NotificationApiTestIT extends TestContainers {

    @MockitoSpyBean
    private NotifyConsumer notifyConsumer;

    @Autowired
    private WireMockServer wireMockServer;

    @Test
    @Order(1)
    @DisplayName("notification api should return 204 no content when service is up")
    void notificationApi_ShouldReturnNoContent_WhenServiceIsUp() {

        stubFor(post(urlEqualTo("/api/v1/notify"))
                .willReturn(aResponse()
                        .withStatus(204)
                        .withHeader("Content-Type", "application/json")
                ));

        notifyConsumer.receive(AFTER_TRANSACTION_SUCCESS);

    }

    @Test
    @Order(2)
    @DisplayName("notification api should return 504 gateway timeout when service is down")
    void notificationApi_ShouldReturnGatewayTimeout_WhenServiceIsDown() {

        stubFor(post(urlEqualTo("/api/v1/notify"))
                .willReturn(aResponse()
                        .withStatus(504)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("post_notifyservicefailed_504.json")
                ));

        Assertions.assertThatException()
                .isThrownBy(() -> notifyConsumer.receive(BDDMockito.any(Transaction.class)));

    }

}
