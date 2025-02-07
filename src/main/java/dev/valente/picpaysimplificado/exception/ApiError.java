package dev.valente.picpaysimplificado.exception;

import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
public class ApiError {
    private OffsetDateTime timestamp;
    private int statusCode;
    private String error;
    private String message;
    private String path;
}
