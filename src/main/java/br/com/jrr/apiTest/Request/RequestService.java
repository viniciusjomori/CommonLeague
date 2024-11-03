package br.com.jrr.apiTest.Request;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import br.com.jrr.apiTest.Request.Enum.RequestMethod;

@Service
public class RequestService {
    
    @Autowired
    private RequestRepository repository;

    @Autowired
    private RequestUtil requestUtil;

    @Autowired
    private Gson gson;

    public HttpDTO request(String endpoint, RequestMethod method) {
        return request(endpoint, method, null);
    }

    public HttpDTO request(String endpoint, RequestMethod method, Object body) {
        String jsonBody = (body != null) ? gson.toJson(body) : null;

        RequestEntity entity = RequestEntity.builder()
            .endpoint(endpoint)
            .method(method)
            .body(jsonBody)
            .build();
        
        entity = repository.save(entity);

        return request(entity);
    }

    public HttpDTO request(RequestEntity entity) {
        HttpDTO httpDTO = requestUtil.request(
            entity.getEndpoint(),
            entity.getMethod().toHttpMethod(),
            entity.getBody()
        );

        String jsonBody = httpDTO.jsonBody();
        jsonBody = jsonBody.length() > 1000 ? jsonBody.substring(0, 1000) : jsonBody;

        entity.setResponse(jsonBody);
        entity.setResponseDate(LocalDateTime.now());
        entity.setHttpStatus(httpDTO.statusCode());
        repository.save(entity);

        return httpDTO;
    }
}
