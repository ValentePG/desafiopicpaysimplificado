package dev.valente.picpaysimplificado.service;

import dev.valente.picpaysimplificado.domain.Wallet;
import dev.valente.picpaysimplificado.exception.InconsistencyException;
import dev.valente.picpaysimplificado.exception.WalletNotFoundException;
import dev.valente.picpaysimplificado.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    public List<Wallet> getWalletsForTransaction(long payeeId, long payerId) {

        var payeeWallet = assertThatWalletExist(payeeId);
        var payerWallet = assertThatWalletExist(payerId);

        return List.of(payeeWallet, payerWallet);
    }

    public void updateWallets(Wallet payeeWallet, Wallet payerWallet, BigDecimal transactionAmount) {
        var newBalanceForPayee = payeeWallet.getBalance().add(transactionAmount);
        var newBalanceForPayer = payerWallet.getBalance().subtract(transactionAmount);

        payeeWallet.setBalance(newBalanceForPayee);
        payerWallet.setBalance(newBalanceForPayer);

        updateWallet(payeeWallet);
        updateWallet(payerWallet);

    }

    private void updateWallet(Wallet wallet) {
        var rowsAffected = walletRepository.updateWallet(wallet);
        if (rowsAffected == 0) throw new InconsistencyException("Erro de inconsistência na transação. Wallet ID: "
                + wallet.getId());
        wallet.setVersion(wallet.getVersion() + 1);
    }

    private Wallet assertThatWalletExist(long walletId) {
        return walletRepository.getWallet(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet id: %s não existe".formatted(walletId)));
    }
}
