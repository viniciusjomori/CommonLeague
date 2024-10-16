package br.com.jrr.apiTest.Request;

public record HttpDTO(
    int statusCode,
    String jsonBody
) {
    
}
