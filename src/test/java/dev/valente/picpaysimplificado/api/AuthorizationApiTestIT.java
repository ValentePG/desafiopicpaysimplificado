package dev.valente.picpaysimplificado.api;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import dev.valente.picpaysimplificado.config.RestAssuredConfig;
import dev.valente.picpaysimplificado.config.TestContainers;
import dev.valente.picpaysimplificado.domain.Transaction;
import dev.valente.picpaysimplificado.repository.TransactionRepository;
import dev.valente.picpaysimplificado.service.NotifyService;
import dev.valente.picpaysimplificado.utils.FileUtils;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import net.javacrumbs.jsonunit.assertj.JsonAssertions;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.jdbc.Sql;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = RestAssuredConfig.class)
@AutoConfigureWireMock(port = 0, files = "classpath:/wiremock/authorizationapi")
public class AuthorizationApiTestIT extends TestContainers {

    public final String URL = "/v1/transfer";

    @Autowired
    private RequestSpecification requestSpecification;

    @MockitoSpyBean
    private TransactionRepository transactionRepository;

    @MockitoSpyBean
    private NotifyService notifyService;

    @Autowired
    private WireMockServer wireMockServer;

    @Autowired
    private FileUtils fileUtils;

    @BeforeEach
    void setUp() {
        RestAssured.requestSpecification = requestSpecification;
    }


    @Test
    @Order(1)
    @Sql(value = "/sql/initthreewallets.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/dropdata.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("POST v1/transfer should create a transaction with success when all is ok")
    void createTransaction_ShouldCreateTransaction_WhenAllIsOk() throws IOException {;

        createStubsForOkWireMock();

        BDDMockito.doNothing().when(notifyService).notify(BDDMockito.any(Transaction.class));

        var request = fileUtils.readFile("/transaction/post_createtransactionrequest_200.json");
        var responseFromFile = fileUtils.readFile("/transaction/post_createtransactionresponse_200.json");

        var response = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .body(request)
                .when()
                .post(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(Matchers.equalTo(responseFromFile))
                .log().all();


        var transactions = transactionRepository.getAllTransactions();

        Assertions.assertThat(transactions)
                .isNotEmpty()
                .hasSize(1);

        var transactionCreated = transactions.getFirst();

        Assertions.assertThat(transactionCreated)
                .matches(t -> t.getPayeeWalletId().equals(2L))
                .matches(t -> t.getPayerWalletId().equals(1L))
                .matches(t -> t.getAmount().equals(BigDecimal.valueOf(50.0)
                        .setScale(2, RoundingMode.HALF_UP)));


    }

    @Test
    @Order(2)
    @Sql(value = "/sql/initthreewallets.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/dropdata.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("POST v1/transfer should return NotAuthorized exception when is not authorized")
    void createTransaction_ShouldReturnNotAuthorizedException_WhenTransactionIsNotAuthorized() {

        createStubsForForbiddenWireMock();

        var request = fileUtils.readFile("/transaction/post_createtransactionrequest_200.json");
        var responseBackEndExpected = fileUtils.readFile("/transaction/post_createtransactionnotauthorized_403.json");

        var response = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .body(request)
                .when()
                .post(URL)
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .log().all()
                .extract().response().asString();

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("timestamp")
                .isEqualTo(responseBackEndExpected);

    }

//    private void removeStubsForForbiddenWireMock() {
//        var stubsId = wireMockServer.getStubMappings().stream()
//                .filter(s -> s.getResponse().getStatus() == HttpStatus.FORBIDDEN.value())
//                .map(StubMapping::getId);
//
//        stubsId.forEach(a -> wireMockServer.removeStubMapping(a));
//    }

    private void createStubsForForbiddenWireMock() {
        stubFor(get(urlEqualTo("/api/v2/authorize"))
                .willReturn(aResponse()
                        .withStatus(403)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("get_notauthorizedtransactionapi_403.json")
                ));
    }

    private void createStubsForOkWireMock() {
        stubFor(get(urlEqualTo("/api/v2/authorize"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("get_authorizedtransactionapi_200.json")
                ));
    }
}
