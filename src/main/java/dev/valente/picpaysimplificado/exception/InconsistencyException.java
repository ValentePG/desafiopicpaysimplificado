package dev.valente.picpaysimplificado.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InconsistencyException extends ResponseStatusException {
    public InconsistencyException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
