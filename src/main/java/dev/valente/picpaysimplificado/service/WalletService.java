package dev.valente.picpaysimplificado.service;

import dev.valente.picpaysimplificado.domain.Wallet;
import dev.valente.picpaysimplificado.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    public List<Wallet> getWallets(long payeeId, long payerId) {
        return walletRepository.getWallets(payeeId, payerId);
    }

    public void updateWallets(Wallet payeeWallet, Wallet payerWallet, BigDecimal transactionAmount) {
        var newBalanceForPayee = payeeWallet.getBalance().add(transactionAmount);
        var newBalanceForPayer = payerWallet.getBalance().subtract(transactionAmount);

        payeeWallet.setBalance(newBalanceForPayee);
        payerWallet.setBalance(newBalanceForPayer);

        walletRepository.updateWallets(payeeWallet, payerWallet);
    }
}
