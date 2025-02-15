package dev.valente.picpaysimplificado.domain;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;

@Getter
@NoArgsConstructor
@Builder
@ToString
public class Transaction implements Serializable {

    private long id;
    private BigDecimal amount;
    private Long payeeWalletId;
    private Long payerWalletId;
    @Setter
    private OffsetDateTime date;

    public Transaction(long id, BigDecimal amount, Long payeeWalletId, Long payerWalletId, OffsetDateTime date) {
        this.id = id;
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
        this.payeeWalletId = payeeWalletId;
        this.payerWalletId = payerWalletId;
        this.date = date;
    }
}
