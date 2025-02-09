package dev.valente.picpaysimplificado.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InconsistencyError extends ResponseStatusException {
    public InconsistencyError(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
