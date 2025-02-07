package dev.valente.picpaysimplificado.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class WalletTypeNotValidForTransactionException extends ResponseStatusException {
    public WalletTypeNotValidForTransactionException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
