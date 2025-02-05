package dev.valente.picpaysimplificado.service;

import dev.valente.picpaysimplificado.domain.Transaction;
import dev.valente.picpaysimplificado.repository.TransactionRepository;
import dev.valente.picpaysimplificado.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    public Transaction createTransaction(Transaction transaction) {

        var payerWalletId = transaction.getPayer_wallet_id();
        var payeeWalletId = transaction.getPayee_wallet_id();
        var payeeWallet = walletRepository.getWallet(payeeWalletId);
        var payerWallet = walletRepository.getWallet(payerWalletId);
        var payeeBalance = payeeWallet.getBalance();
        var payerBalance = payerWallet.getBalance();
        var transactionAmount = transaction.getAmount();

        assertThatBalanceIsGreaterThanAmount(transactionAmount, payeeBalance);

        simulaAutorizacao();

        var newTransaction = Transaction.builder()
                .date(OffsetDateTime.now())
                .amount(transactionAmount)
                .payee_wallet_id(payeeWalletId)
                .payer_wallet_id(payerWalletId)
                .build();

        var amountForPayee = payeeBalance.subtract(transactionAmount);
        var amountForPayer = payerBalance.add(transactionAmount);

        transactionRepository.saveTransaction(newTransaction);
        walletRepository.updateWallet(amountForPayee, payeeWallet);
        walletRepository.updateWallet(amountForPayer, payerWallet);

        return transaction;
    }

    private void assertThatBalanceIsGreaterThanAmount(BigDecimal amount, BigDecimal balance) {
        if(amount.compareTo(balance) >= 0) throw new RuntimeException();
    }

    private void simulaAutorizacao(){}
}
