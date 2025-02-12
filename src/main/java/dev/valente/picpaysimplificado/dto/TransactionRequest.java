package dev.valente.picpaysimplificado.dto;

import dev.valente.picpaysimplificado.exception.ArgumentNotValidException;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record TransactionRequest(@Schema(description = "Valor da transação", example = "50.0") BigDecimal value,
                                 @Schema(description = "Id do pagador", example = "1") @NotNull Long payer,
                                 @Schema(description = "Id do recebedor", example = "2") @NotNull Long payee) {

    public TransactionRequest {
        if(value == null) throw new ArgumentNotValidException("Campo valor não deve estar vazio");
        value = value.setScale(2, RoundingMode.HALF_UP);
    }
}
