package dev.valente.picpaysimplificado.service;

import dev.valente.picpaysimplificado.domain.Wallet;
import dev.valente.picpaysimplificado.exception.InconsistencyError;
import dev.valente.picpaysimplificado.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    public List<Wallet> getWallets(long payeeId, long payerId) {
        return walletRepository.getWalletsForTransaction(payeeId, payerId);
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
        if(rowsAffected == 0) throw new InconsistencyError("Erro de inconsistência na transação. Wallet ID: "
                + wallet.getId());
        wallet.setVersion(wallet.getVersion() + 1);
    }
}
