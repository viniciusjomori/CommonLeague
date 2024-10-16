package br.com.jrr.apiTest.RiotAccount.Strategy;

import org.springframework.stereotype.Component;

@Component
public class RiotAccNotFound implements IRiotAccError {

    @Override
    public String getMessage() {
        return "Riot account not found";
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
    
}
