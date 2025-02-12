package dev.valente.picpaysimplificado.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ApiError> handleInsufficientBalanceException(InsufficientBalanceException ex,
                                                                       HttpServletRequest request) {
        var apiError = ApiError.builder()
                .timestamp(OffsetDateTime.now())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getReason())
                .path(request.getRequestURI())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
                                                                 HttpServletRequest request) {
        var defaultMessage = e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .filter(Objects::nonNull)
                .sorted()
                .collect(Collectors.joining(", "));


        var apiError = ApiError.builder()
                .timestamp(OffsetDateTime.now())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(defaultMessage)
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(WalletTypeNotValidForTransactionException.class)
    public ResponseEntity<ApiError> handleWalletTypeNotValidForTransactionException(WalletTypeNotValidForTransactionException ex,
                                                                                    HttpServletRequest request) {
        var apiError = ApiError.builder()
                .timestamp(OffsetDateTime.now())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getReason())
                .path(request.getRequestURI())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(InconsistencyException.class)
    public ResponseEntity<ApiError> handleInconsistentException(InconsistencyException ex,
                                                                HttpServletRequest request) {
        var apiError = ApiError.builder()
                .timestamp(OffsetDateTime.now())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getReason())
                .path(request.getRequestURI())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(NotAuthorizedException.class)
    public ResponseEntity<ApiError> handleWalletNotFoundException(NotAuthorizedException ex,
                                                                  HttpServletRequest request) {
        var apiError = ApiError.builder()
                .timestamp(OffsetDateTime.now())
                .error(HttpStatus.FORBIDDEN.getReasonPhrase())
                .message(ex.getReason())
                .path(request.getRequestURI())
                .statusCode(HttpStatus.FORBIDDEN.value())
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiError);
    }

    @ExceptionHandler(WalletNotFoundException.class)
    public ResponseEntity<ApiError> handleWalletNotFoundException(WalletNotFoundException ex,
                                                                  HttpServletRequest request) {
        var apiError = ApiError.builder()
                .timestamp(OffsetDateTime.now())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(ex.getReason())
                .path(request.getRequestURI())
                .statusCode(HttpStatus.NOT_FOUND.value())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    @ExceptionHandler(UnnavailableServiceException.class)
    public ResponseEntity<ApiError> handleUnnavailableService(UnnavailableServiceException ex,
                                                              HttpServletRequest request) {
        var apiError = ApiError.builder()
                .timestamp(OffsetDateTime.now())
                .error(HttpStatus.GATEWAY_TIMEOUT.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .statusCode(HttpStatus.GATEWAY_TIMEOUT.value())
                .build();
        return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(apiError);
    }
}
