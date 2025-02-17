package dev.valente.picpaysimplificado.concurrency;

import dev.valente.picpaysimplificado.config.TestContainers;
import dev.valente.picpaysimplificado.domain.Transaction;
import dev.valente.picpaysimplificado.repository.WalletRepository;
import dev.valente.picpaysimplificado.service.AuthorizationService;
import dev.valente.picpaysimplificado.service.NotifyClient;
import dev.valente.picpaysimplificado.service.TransactionService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OptimisticLockingTestIT extends TestContainers {

    @MockitoSpyBean
    private AuthorizationService authorizationService;

    @MockitoSpyBean
    private NotifyClient notifyClient;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private WalletRepository walletRepository;

    @Test
    @Sql(value = "/sql/initthreewallets.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/dropdata.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("")
    void testOptimisticLocking() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Transaction transaction = Transaction.builder()
                .payeeWalletId(3L)
                .payerWalletId(1L)
                .amount(BigDecimal.valueOf(50.00)).build();

        Transaction transaction2 = Transaction.builder()
                .payeeWalletId(3L)
                .payerWalletId(1L)
                .amount(BigDecimal.valueOf(50.00)).build();

        Function<Throwable, Transaction> handleTransactionError = throwable -> {
            System.err.println("Erro na transação " + throwable.getCause().getMessage());
            return null;
        };

        CompletableFuture<Transaction> completableFuture = CompletableFuture.supplyAsync(
                        () -> transactionService.processTransaction(transaction),
                        executorService)
                .exceptionally(handleTransactionError);

        CompletableFuture<Transaction> completablefuture2 = CompletableFuture.supplyAsync(
                        () -> transactionService.processTransaction(transaction2),
                        executorService)
                .exceptionally(handleTransactionError);

        BDDMockito.doNothing().when(authorizationService).getAuthorization();
        BDDMockito.doNothing().when(notifyClient).pushNotification();

        CompletableFuture.allOf(completableFuture, completablefuture2).join();

        executorService.shutdown();

        var walletVersionTest = walletRepository.getWallet(transaction.getPayerWalletId());

        List<CompletableFuture<Transaction>> completableFutureList = List.of(completableFuture, completablefuture2);

        var nullList = completableFutureList.stream().filter(c -> c.join() == null)
                .toList();

        var completedList = completableFutureList.stream().filter(c -> c.join() != null)
                .toList();

        Assertions.assertThat(nullList).hasSize(1);
        Assertions.assertThat(completedList).hasSize(1);

        Assertions.assertThat(walletVersionTest)
                .get()
                .hasFieldOrPropertyWithValue("version", 1)
                .hasFieldOrPropertyWithValue("balance",
                        BigDecimal.valueOf(100.0).setScale(2, BigDecimal.ROUND_HALF_UP));

    }
}
