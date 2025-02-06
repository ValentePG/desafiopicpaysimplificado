package dev.valente.picpaysimplificado.service;

import dev.valente.picpaysimplificado.domain.Transaction;
import dev.valente.picpaysimplificado.domain.Wallet;
import dev.valente.picpaysimplificado.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    private final WalletService walletService;

    @Transactional
    public Transaction processTransaction(Transaction transaction) {

        var payerWalletId = transaction.getPayerWalletId();
        var payeeWalletId = transaction.getPayeeWalletId();
        var transactionAmount = transaction.getAmount();

        var listOfWallets = walletService.getWallets(payeeWalletId, payerWalletId);
        var payeeWallet = listOfWallets.stream().filter(w -> w.getId() == payeeWalletId).findFirst().get();
        var payerWallet = listOfWallets.stream().filter(w -> w.getId() == payerWalletId).findFirst().get();
        var payeeBalance = payeeWallet.getBalance();

        assertThatBalanceIsGreaterThanAmount(transactionAmount, payeeBalance);

        simulaAutorizacao();

        walletService.updateWallets(payeeWallet, payerWallet, transactionAmount);

        return createTransaction(transaction);
    }

    private void assertThatBalanceIsGreaterThanAmount(BigDecimal amount, BigDecimal balance) {
        if(amount.compareTo(balance) > 0) throw new RuntimeException();
    }

    private void simulaAutorizacao(){}

    private Transaction createTransaction(Transaction transaction) {

        var newTransaction = Transaction.builder()
                .date(OffsetDateTime.now())
                .amount(transaction.getAmount())
                .payeeWalletId(transaction.getPayeeWalletId())
                .payerWalletId(transaction.getPayerWalletId())
                .build();

        return transactionRepository.saveTransaction(newTransaction);
    }
}
