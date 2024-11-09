package br.com.jrr.apiTest.App.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ConflictException extends ResponseStatusException {

    public ConflictException(String error) {
        super(HttpStatus.CONFLICT, error);
    }
}

