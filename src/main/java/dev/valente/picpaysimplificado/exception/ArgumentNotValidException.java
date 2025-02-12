package dev.valente.picpaysimplificado.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ArgumentNotValidException extends ResponseStatusException {
    public ArgumentNotValidException(String message) {
        super(HttpStatus.BAD_REQUEST,message);
    }
}
