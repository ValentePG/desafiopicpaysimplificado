package dev.valente.picpaysimplificado.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Builder
public record TransactionResponse(long id, long payer, long payee, BigDecimal amount) {

    public TransactionResponse{
        amount = amount.setScale(2, RoundingMode.HALF_UP);
    }
}
