package br.com.jrr.apiTest.RiotAccount.Strategy;

import org.springframework.stereotype.Component;

@Component
public class RiotAccBadRequest implements IRiotAccError {

    @Override
    public String getMessage() {
        return "Bad request";
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
    
}
