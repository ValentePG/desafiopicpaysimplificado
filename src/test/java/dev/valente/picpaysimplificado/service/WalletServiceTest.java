package dev.valente.picpaysimplificado.service;

import dev.valente.picpaysimplificado.exception.InconsistencyException;
import dev.valente.picpaysimplificado.exception.WalletNotFoundException;
import dev.valente.picpaysimplificado.repository.WalletRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static dev.valente.picpaysimplificado.utils.TransactionUtils.TRANSACTION_AMOUNT;
import static dev.valente.picpaysimplificado.utils.WalletUtils.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WalletServiceTest {

    @InjectMocks
    private WalletService walletService;

    @Mock
    private WalletRepository walletRepository;

    @Test
    @Order(1)
    @DisplayName("getWalletsForTransaction should return a list of wallets when ids were found")
    void getWalletsForTransaction_ShouldReturnWallets_WhenIdsWereFound() {

        BDDMockito.when(walletRepository.getWallet(PAYEE_WALLET.getId())).thenReturn(Optional.of(PAYEE_WALLET));
        BDDMockito.when(walletRepository.getWallet(PAYER_WALLET.getId())).thenReturn(Optional.of(PAYER_WALLET));

        var sut = walletService.getWalletsForTransaction(PAYEE_WALLET.getId(), PAYER_WALLET.getId());

        Assertions.assertThat(sut)
                .isEqualTo(LIST_OF_WALLETS);
    }

    @Test
    @Order(2)
    @DisplayName("getWalletsForTransaction should throw exception when ids were not found")
    void getWalletsForTransaction_ShouldThrowWalletNotFoundException_WhenIdsWereNotFound() {

        BDDMockito.when(walletRepository.getWallet(PAYEE_WALLET.getId())).thenReturn(Optional.of(PAYEE_WALLET));
        BDDMockito.when(walletRepository.getWallet(PAYER_WALLET.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> walletService.getWalletsForTransaction(PAYEE_WALLET.getId(), PAYER_WALLET.getId()))
                .withMessage("404 NOT_FOUND \"Wallet id: %s não existe\"".formatted(PAYER_WALLET.getId()))
                .isInstanceOf(WalletNotFoundException.class);
    }

    @Test
    @Order(3)
    @DisplayName("updateWallets should update wallets")
    void updateWallets_ShouldUpdateWallets_WhenDoesNotHaveInconsistency() {
        BDDMockito.when(walletRepository.updateWallet(PAYEE_WALLET)).thenReturn(1);
        BDDMockito.when(walletRepository.updateWallet(PAYER_WALLET)).thenReturn(1);

        Assertions.assertThatNoException()
                .isThrownBy(() -> walletService.updateWallets(PAYEE_WALLET, PAYER_WALLET, TRANSACTION_AMOUNT));
    }

    @Test
    @Order(4)
    @DisplayName("updateWallets should return InconsistencyException when data has inconsistency")
    void updateWallets_ShouldReturnInconsistencyException_WhenDataHasInconsistency() {
        BDDMockito.when(walletRepository.updateWallet(PAYEE_WALLET)).thenReturn(0);

        Assertions.assertThatException()
                .isThrownBy(() -> walletService.updateWallets(PAYEE_WALLET, PAYER_WALLET, TRANSACTION_AMOUNT))
                .withMessage("400 BAD_REQUEST \"Erro de inconsistência na transação. Wallet ID: %s\""
                        .formatted(PAYEE_WALLET.getId()))
                .isInstanceOf(InconsistencyException.class);
    }
}