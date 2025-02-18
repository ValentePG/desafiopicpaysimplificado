package dev.valente.picpaysimplificado.controller;

import dev.valente.picpaysimplificado.config.RestAssuredConfig;
import dev.valente.picpaysimplificado.config.TestContainers;
import dev.valente.picpaysimplificado.domain.Transaction;
import dev.valente.picpaysimplificado.exception.NotAuthorizedException;
import dev.valente.picpaysimplificado.repository.TransactionRepository;
import dev.valente.picpaysimplificado.service.AuthorizationService;
import dev.valente.picpaysimplificado.service.NotifyConsumer;
import dev.valente.picpaysimplificado.service.NotifyService;
import dev.valente.picpaysimplificado.utils.FileUtils;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import net.javacrumbs.jsonunit.assertj.JsonAssertions;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.jdbc.Sql;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = RestAssuredConfig.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TransactionControllerTestIT extends TestContainers {

    public final String URL = "/v1/transfer";

    @Autowired
    private RequestSpecification requestSpecification;

    @Autowired
    private FileUtils fileUtils;

    @MockitoSpyBean
    private NotifyConsumer notifyConsumer;

    @MockitoSpyBean
    private TransactionRepository transactionRepository;

    @MockitoSpyBean
    private AuthorizationService authorizationService;

    @MockitoSpyBean
    private NotifyService notifyService;

    @BeforeEach
    void setUp() {
        RestAssured.requestSpecification = requestSpecification;
    }

    @Test
    @Order(1)
    @Sql(value = "/sql/initthreewallets.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/dropdata.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("POST v1/transfer should create a transaction with success when all is ok")
    void createTransaction_ShouldCreateTransaction_WhenAllIsOk() throws IOException {

        BDDMockito.doNothing().when(authorizationService).getAuthorization();
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


        BDDMockito.doThrow(new NotAuthorizedException("Authorization[status=fail, data=Data[authorization=false]]"))
                .when(authorizationService).getAuthorization();

        var request = fileUtils.readFile("/transaction/post_createtransactionrequest_200.json");
        var responseFile = fileUtils.readFile("/transaction/post_createtransactionnotauthorized_403.json");

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
                .isEqualTo(responseFile);

    }

    @ParameterizedTest
    @MethodSource("parameterizedValidationOfData")
    @Order(3)
    @DisplayName("POST v1/transfer should return BAD REQUEST when data is invalid")
    @Sql(value = "/sql/initthreewallets.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/dropdata.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createTransaction_ShouldReturnBADRequest_WhenDataIsInvalid(String path, String responseFile) {

        var request = fileUtils.readFile(path);

        var responseFromFile = fileUtils.readFile(responseFile);

        var response = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .body(request)
                .when()
                .post(URL)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .log().all()
                .extract().response().asString();

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("timestamp")
                .isEqualTo(responseFromFile);
    }

    private static Stream<Arguments> parameterizedValidationOfData() {

        var request1 = "/transaction/post_createtransactionwithdatainvalid1_400.json";
        var request2 = "/transaction/post_createtransactionwithdatainvalid2_400.json";
        var request3 = "/transaction/post_createtransactionrequest2_200.json";
        var response1 = "/transaction/post_createtransactionargumentnotvalid1_400.json";
        var response2 = "/transaction/post_createtransactionargumentnotvalid2_400.json";
        var response3 = "/transaction/post_createtransactioninsufficientbalance_400.json";

        return Stream.of(
                Arguments.of(request1, response1),
                Arguments.of(request2, response2),
                Arguments.of(request3, response3)
        );
    }

    @Test
    @Order(4)
    @Sql(value = "/sql/initthreewallets.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/dropdata.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("POST v1/transfer Should return Bad request when balance is less than the value of transfer")
    void createTransaction_ShouldReturnBadRequest_WhenBalanceIsLessThanTheValueOfTransfer() {

        var request = fileUtils.readFile("/transaction/post_createtransactionforwalletwithnobalance_400.json");
        var responseFile = fileUtils.readFile("/transaction/post_createtransactionwalletwithnobalance_400.json");

        var response = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .body(request)
                .when()
                .post(URL)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .log().all()
                .extract().response().asString();

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("timestamp")
                .isEqualTo(responseFile);

    }

    @Test
    @Order(5)
    @Sql(value = "/sql/initthreewallets.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/dropdata.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("POST v1/transfer Should return Bad request when walletType is shopkeeper")
    void createTransaction_ShouldReturnBadRequest_WhenWalletTypeIsShopkeeper() {

        var request = fileUtils.readFile("/transaction/post_createtransactionwithpayershopkeep_400.json");
        var responseFile = fileUtils.readFile("/transaction/post_createtransactionwithpayershopkeeper_400.json");

        var response = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .body(request)
                .when()
                .post(URL)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .log().all()
                .extract().response().asString();

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("timestamp")
                .isEqualTo(responseFile);

    }

}