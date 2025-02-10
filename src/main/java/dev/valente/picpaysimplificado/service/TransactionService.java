package dev.valente.picpaysimplificado.service;

import dev.valente.picpaysimplificado.domain.Transaction;
import dev.valente.picpaysimplificado.domain.Wallet;
import dev.valente.picpaysimplificado.domain.WalletType;
import dev.valente.picpaysimplificado.exception.InsufficientBalanceException;
import dev.valente.picpaysimplificado.exception.WalletTypeNotValidForTransactionException;
import dev.valente.picpaysimplificado.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    private final WalletService walletService;
    private final AuthorizationService authorizationService;
    private final NotifyService notifyService;

    @Transactional
    public Transaction processTransaction(Transaction transaction) {

        var payerWalletId = transaction.getPayerWalletId();
        var payeeWalletId = transaction.getPayeeWalletId();
        var transactionAmount = transaction.getAmount();

        var wallets = walletService.getWallets(payeeWalletId, payerWalletId)
                .stream()
                .collect(Collectors.toMap(Wallet::getId, wallet -> wallet));

        var payeeWallet = wallets.get(payeeWalletId);
        var payerWallet = wallets.get(payerWalletId);

        validateTransaction(payerWallet, transactionAmount);

        walletService.updateWallets(payeeWallet, payerWallet, transactionAmount);

        var transactionSuccess = createTransaction(transaction);

        notifyService.notify(transactionSuccess);

        return transactionSuccess;
    }

    private Transaction createTransaction(Transaction transaction) {

        var offsetDatetime = OffsetDateTime.now();

        var newTransaction = Transaction.builder()
                .date(offsetDatetime)
                .amount(transaction.getAmount())
                .payeeWalletId(transaction.getPayeeWalletId())
                .payerWalletId(transaction.getPayerWalletId())
                .build();

        return transactionRepository.saveTransaction(newTransaction);
    }

    private void validateTransaction(Wallet payerWallet, BigDecimal amount) {
        checkIfPayerIsShopkeeper(payerWallet.getWalletType());
        assertThatBalanceIsGreaterThanAmount(amount, payerWallet.getBalance());
        authorizationService.getAuthorization();
    }

    private void assertThatBalanceIsGreaterThanAmount(BigDecimal amount, BigDecimal balance) {
        if (balance.compareTo(amount) < 0)
            throw new InsufficientBalanceException("Saldo insuficiente para completar transação");
    }

    private void checkIfPayerIsShopkeeper(WalletType walletType) {
        if (walletType.getValue() == 2)
            throw new WalletTypeNotValidForTransactionException("Lojistas não podem fazer transações");
    }

}
