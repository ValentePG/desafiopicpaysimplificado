package dev.valente.picpaysimplificado.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@Builder
public class Transaction implements Serializable {

    private long id;
    private BigDecimal amount;
    private Long payeeWalletId;
    private Long payerWalletId;
    private OffsetDateTime date;

    public Transaction(long id, BigDecimal amount, Long payeeWalletId, Long payerWalletId, OffsetDateTime date) {
        this.id = id;
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
        this.payeeWalletId = payeeWalletId;
        this.payerWalletId = payerWalletId;
        this.date = date;
    }
}
