package br.com.jrr.apiTest.RiotAccount.Strategy;

import br.com.jrr.apiTest.Request.HttpDTO;

public interface IRiotAccError {
    
    String getMessage();
    int getStatusCode();

    default boolean isError(HttpDTO dto) {
        return dto.statusCode() == getStatusCode();
    };

}
