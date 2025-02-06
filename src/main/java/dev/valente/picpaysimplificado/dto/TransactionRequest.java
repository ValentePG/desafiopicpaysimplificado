package dev.valente.picpaysimplificado.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record TransactionRequest(BigDecimal value, long payer, long payee) {

    public TransactionRequest {
        value = value.setScale(2, RoundingMode.HALF_UP);
    }
}
