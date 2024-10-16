package br.com.jrr.apiTest.service.APIConfigService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConvertData implements IConvetData {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public <T> T getDate(String json, Class<T> classe){
        try{
            return mapper.readValue(json, classe);
        }catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }

    }
}
