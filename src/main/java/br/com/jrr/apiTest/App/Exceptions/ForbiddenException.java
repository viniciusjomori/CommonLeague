package br.com.jrr.apiTest.App.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ForbiddenException extends ResponseStatusException {

    public ForbiddenException(String error) {
        super(HttpStatus.FORBIDDEN, error);
    }
    
}
