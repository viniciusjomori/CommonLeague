package br.com.jrr.apiTest.App.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NotFoundException extends ResponseStatusException {

    public NotFoundException(String error) {
        super(HttpStatus.NOT_FOUND, error);
    }
}
