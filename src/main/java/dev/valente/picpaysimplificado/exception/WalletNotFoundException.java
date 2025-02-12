package dev.valente.picpaysimplificado.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class WalletNotFoundException extends ResponseStatusException {
    public WalletNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND,message);
    }
}
