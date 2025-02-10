package dev.valente.picpaysimplificado.exception;

import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;

@Builder
@Getter
public class ApiError {
    private OffsetDateTime timestamp;
    private int statusCode;
    private String error;
    private String message;
    private String path;
}
