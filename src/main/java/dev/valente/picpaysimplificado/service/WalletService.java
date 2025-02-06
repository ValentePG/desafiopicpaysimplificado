package dev.valente.picpaysimplificado.service;

import dev.valente.picpaysimplificado.domain.Wallet;
import dev.valente.picpaysimplificado.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    public List<Wallet> getWallets(long payeeId, long payerId) {
        return walletRepository.getWallets(payeeId, payerId);
    }

    public void updateWallets(Wallet payeeWallet, Wallet payerWallet, BigDecimal transactionAmount) {
        var newBalanceForPayee = payeeWallet.getBalance().subtract(transactionAmount);
        var newBalanceForPayer = payerWallet.getBalance().add(transactionAmount);
        payeeWallet.setBalance(newBalanceForPayee);
        payerWallet.setBalance(newBalanceForPayer);
        walletRepository.updateWallets(payeeWallet, payerWallet);
    }
}
