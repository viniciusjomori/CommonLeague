package br.com.jrr.apiTest.Request;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import com.google.gson.Gson;

@Configuration
public class RequestConfig {
    
    @Bean
    public WebClient webClient() {
        return WebClient
            .builder()
            .build();
    }

    @Bean
    public Gson gson() {
        return new Gson();
    }
}
