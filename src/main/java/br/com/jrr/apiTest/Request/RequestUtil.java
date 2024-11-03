package br.com.jrr.apiTest.Request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Component
public class RequestUtil {

    @Autowired
    private WebClient webClient;

    public HttpDTO request(String endpoint, HttpMethod method, String requestBody) {
        return webClient.method(method)
            .uri(endpoint)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.justOrEmpty(requestBody), String.class)
            .exchangeToMono(response -> response
                .toEntity(String.class)
                .map(entity -> new HttpDTO(entity.getStatusCode().value(), entity.getBody())))
            .block();
    }
    
}