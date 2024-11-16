package br.com.jrr.apiTest.App.Exceptions;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class BadRequestException extends ResponseStatusException {

    public BadRequestException(String error) {
        super(HttpStatus.BAD_REQUEST, error);
    }

    public BadRequestException(List<String> errors) {
        super(HttpStatus.BAD_REQUEST, errors.toString());
    }
}