package dev.valente.picpaysimplificado.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
public class Transactions {

    private int id;
    private BigDecimal amount;
    private Long payee;
    private Long payer;
    private OffsetDateTime date;

    public Transactions(int id, BigDecimal amount, Long payee, Long payer) {
        this.id = id;
        this.amount = amount;
        this.payee = payee;
        this.payer = payer;
        this.date = OffsetDateTime.now();
    }
}
