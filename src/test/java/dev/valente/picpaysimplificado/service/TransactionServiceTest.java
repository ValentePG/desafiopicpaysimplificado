package dev.valente.picpaysimplificado.service;

import dev.valente.picpaysimplificado.domain.Transaction;
import dev.valente.picpaysimplificado.exception.NotAuthorizedException;
import dev.valente.picpaysimplificado.repository.TransactionRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static dev.valente.picpaysimplificado.utils.TransactionUtils.AFTER_TRANSACTION_SUCCESS;
import static dev.valente.picpaysimplificado.utils.TransactionUtils.INITIAL_TRANSACTION;
import static dev.valente.picpaysimplificado.utils.WalletUtils.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private WalletService walletService;

    @Mock
    private AuthorizationService authorizationService;

    @Mock
    private NotifyService notifyService;

    @Test
    @Order(1)
    @DisplayName("Should process transaction successfully")
    void processTransaction_ShouldProcessTransactionSuccessfully() {

        BDDMockito.when(walletService.getWalletsForTransaction(INITIAL_TRANSACTION.getPayeeWalletId(),
                INITIAL_TRANSACTION.getPayerWalletId())).thenReturn(LIST_OF_WALLETS);

        BDDMockito.doNothing().when(authorizationService).getAuthorization();
        BDDMockito.doNothing().when(walletService).updateWallets(PAYEE_WALLET, PAYER_WALLET, INITIAL_TRANSACTION.getAmount());
        BDDMockito.doNothing().when(notifyService).notify(AFTER_TRANSACTION_SUCCESS);

        BDDMockito.when(transactionRepository.saveTransaction(BDDMockito.any(Transaction.class)))
                .thenReturn(AFTER_TRANSACTION_SUCCESS);

        var sut = transactionService.processTransaction(INITIAL_TRANSACTION);

        Assertions.assertThat(sut)
                .hasNoNullFieldsOrProperties()
                .isEqualTo(AFTER_TRANSACTION_SUCCESS);
    }

    @Test
    @Order(2)
    @DisplayName("Should throw NotAuthorizedException when transaction is not authorized")
    void processTransaction_ShouldReturnNotAuthorizedTransaction_whenTransactionIsNotAuthorized() {

        BDDMockito.when(walletService.getWalletsForTransaction(INITIAL_TRANSACTION.getPayeeWalletId(),
                INITIAL_TRANSACTION.getPayerWalletId())).thenReturn(LIST_OF_WALLETS);

        BDDMockito.doThrow(new NotAuthorizedException("Authorization[status=fail, data=Data[authorization=false]]"))
                .when(authorizationService).getAuthorization();

        Assertions.assertThatException()
                .isThrownBy(() -> transactionService.processTransaction(INITIAL_TRANSACTION))
                .withMessage("403 FORBIDDEN \"Authorization[status=fail, data=Data[authorization=false]]\"")
                .isInstanceOf(NotAuthorizedException.class);
    }

}