package dev.valente.picpaysimplificado.rabbitmq;

import dev.valente.picpaysimplificado.config.TestContainers;
import dev.valente.picpaysimplificado.domain.Transaction;
import dev.valente.picpaysimplificado.service.NotifyClient;
import dev.valente.picpaysimplificado.service.NotifyConsumer;
import dev.valente.picpaysimplificado.service.NotifyService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.TimeUnit;

import static dev.valente.picpaysimplificado.utils.TransactionUtils.AFTER_TRANSACTION_SUCCESS;
import static org.awaitility.Awaitility.await;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RabbitMqTestIT extends TestContainers {

    @Autowired
    private NotifyService notifyService;

    @MockitoSpyBean
    private NotifyConsumer notifyConsumer;

    @MockitoSpyBean
    private NotifyClient notifyClient;

    @Test
    @Order(1)
    @DisplayName("notify() should deliver a message when successfull")
    void notify_ShouldDeliverAmessage_WhenSuccessfull() {

        notifyService.notify(AFTER_TRANSACTION_SUCCESS);

        var timestamp = AFTER_TRANSACTION_SUCCESS.getDate();

        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);

        BDDMockito.doNothing().when(notifyClient).pushNotification();

        await().atMost(10, TimeUnit.SECONDS).untilAsserted(() ->
                BDDMockito.verify(notifyConsumer).receive(captor.capture()));


        var receivedTransaction = captor.getValue();

        Assertions.assertThat(receivedTransaction)
                .matches(t -> t.getId() == 1L)
                .matches(t -> t.getPayeeWalletId().equals(2L))
                .matches(t -> t.getPayerWalletId().equals(1L))
                .matches(t -> t.getAmount().equals(BigDecimal.valueOf(50.0)
                        .setScale(2, RoundingMode.HALF_UP)))
                .matches(t -> t.getDate().equals(timestamp));
    }
}
