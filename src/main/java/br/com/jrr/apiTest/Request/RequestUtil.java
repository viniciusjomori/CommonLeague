package br.com.jrr.apiTest.Request;

import java.util.HashMap;
import java.util.Map;

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
        return request(endpoint, method, new HashMap<String, String>(), requestBody);
    }

    public HttpDTO request(String endpoint, HttpMethod method, Map<String, String> header, String requestBody) {
        return webClient.method(method)
            .uri(endpoint)
            .contentType(MediaType.APPLICATION_JSON)
            .headers(httpHeaders -> header.forEach(httpHeaders::add)) 
            .body(Mono.justOrEmpty(requestBody), String.class)
            .exchangeToMono(response -> response
                .toEntity(String.class)
                .map(entity -> new HttpDTO(entity.getStatusCode().value(), entity.getBody())))
            .block();
    }
    
}