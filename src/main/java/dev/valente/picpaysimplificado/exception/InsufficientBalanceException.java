package dev.valente.picpaysimplificado.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InsufficientBalanceException extends ResponseStatusException {
    public InsufficientBalanceException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
