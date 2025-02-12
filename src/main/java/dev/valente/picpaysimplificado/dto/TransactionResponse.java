package dev.valente.picpaysimplificado.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Builder
public record TransactionResponse(
        @Schema(description = "Id da transação", example = "1")
        long id,
        @Schema(description = "Id do pagador", example = "1")
        long payer,
        @Schema(description = "Id do recebedor", example = "2")
        long payee,
        @Schema(description = "Valor da transação", example = "50.00")
        BigDecimal amount) {

    public TransactionResponse {
        amount = amount.setScale(2, RoundingMode.HALF_UP);
    }
}
