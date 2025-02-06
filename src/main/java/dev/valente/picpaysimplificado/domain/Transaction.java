package dev.valente.picpaysimplificado.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@Builder
public class Transaction {

    private int id;
    private BigDecimal amount;
    private Long payeeWalletId;
    private Long payerWalletId;
    private OffsetDateTime date;

    public Transaction(int id, BigDecimal amount, Long payeeWalletId, Long payerWalletId, OffsetDateTime date) {
        this.id = id;
        this.amount = amount;
        this.payeeWalletId = payeeWalletId;
        this.payerWalletId = payerWalletId;
        this.date = date;
    }
}
