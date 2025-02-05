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
    private Long payee_wallet_id;
    private Long payer_wallet_id;
    private OffsetDateTime date;

    public Transaction(int id, BigDecimal amount, Long payee_wallet_id, Long payer_wallet_id, OffsetDateTime date) {
        this.id = id;
        this.amount = amount;
        this.payee_wallet_id = payee_wallet_id;
        this.payer_wallet_id = payer_wallet_id;
        this.date = date;
    }
}
