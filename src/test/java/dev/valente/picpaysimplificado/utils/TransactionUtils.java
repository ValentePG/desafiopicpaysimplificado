package dev.valente.picpaysimplificado.utils;

import dev.valente.picpaysimplificado.domain.Transaction;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Component
public class TransactionUtils {

    public static final String DEFAULT_TIMESTAMP = "2025-02-12T15:10:09.444283-03:00";
    public static final OffsetDateTime DEFAULT_TIMESTAMP_OFFSET = OffsetDateTime.parse(DEFAULT_TIMESTAMP);
    public static final Transaction INITIAL_TRANSACTION = Transaction.builder()
            .amount(BigDecimal.valueOf(50.0))
            .payerWalletId(1L)
            .payeeWalletId(2L)
            .build();

    public static final BigDecimal TRANSACTION_AMOUNT = INITIAL_TRANSACTION.getAmount();

    public static final Transaction AFTER_TRANSACTION_SUCCESS = Transaction.builder()
            .amount(INITIAL_TRANSACTION.getAmount())
            .payeeWalletId(INITIAL_TRANSACTION.getPayeeWalletId())
            .payerWalletId(INITIAL_TRANSACTION.getPayerWalletId())
            .date(DEFAULT_TIMESTAMP_OFFSET)
            .id(1L)
            .build();
}
