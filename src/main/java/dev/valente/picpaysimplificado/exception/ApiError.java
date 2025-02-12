package dev.valente.picpaysimplificado.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;

@Builder
@Getter
public class ApiError {

    @Schema(description = "TimeStamp da exceção", example = "2025-01-30T21:54:43.653-03:00")
    private OffsetDateTime timestamp;
    @Schema(description = "Status code da exceção", example = "400")
    private int statusCode;
    @Schema(description = "Descrição do erro", example = "Bad Request")
    private String error;
    @Schema(description = "Mensagem do erro", example = "O campo valor não pode estar vazio")
    private String message;
    @Schema(description = "Path do erro", example = "/v1/transfer")
    private String path;
}
