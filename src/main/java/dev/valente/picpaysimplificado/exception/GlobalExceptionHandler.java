package dev.valente.picpaysimplificado.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.OffsetDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ApiError> handleInsufficientBalanceException(InsufficientBalanceException ex,
                                                                       HttpServletRequest request) {
        var apiError = ApiError.builder()
                .timestamp(OffsetDateTime.now(ZoneId.of("America/Sao_Paulo")))
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getReason())
                .path(request.getRequestURI())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(WalletTypeNotValidForTransactionException.class)
    public ResponseEntity<ApiError> handleWalletTypeNotValidForTransactionException(WalletTypeNotValidForTransactionException ex,
                                                                                    HttpServletRequest request) {
        var apiError = ApiError.builder()
                .timestamp(OffsetDateTime.now(ZoneId.of("America/Sao_Paulo")))
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getReason())
                .path(request.getRequestURI())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(NotAuthorizedException.class)
    public ResponseEntity<ApiError> handleNotAuthorizedException(NotAuthorizedException ex,
                                                                 HttpServletRequest request) {
        var apiError = ApiError.builder()
                .timestamp(OffsetDateTime.now(ZoneId.of("America/Sao_Paulo")))
                .error(HttpStatus.FORBIDDEN.getReasonPhrase())
                .message(ex.getReason())
                .path(request.getRequestURI())
                .statusCode(HttpStatus.FORBIDDEN.value())
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiError);
    }
}
