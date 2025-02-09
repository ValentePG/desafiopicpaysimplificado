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

        var rowsAffectedPayer = walletRepository.updateWallet(payerWallet);
        if(rowsAffectedPayer == 0) throw new InconsistencyError("Ocorreu um erro de inconsistência na transação payer");
        payerWallet.setVersion(payerWallet.getVersion() + 1);

        var rowsAffectedPayee = walletRepository.updateWallet(payeeWallet);
        if(rowsAffectedPayee == 0 ) throw new InconsistencyError("Ocorreu um erro de inconsistência na transação payee");
        payeeWallet.setVersion(payeeWallet.getVersion() + 1);
    }
}
