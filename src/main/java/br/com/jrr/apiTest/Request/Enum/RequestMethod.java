package br.com.jrr.apiTest.Request.Enum;

import org.springframework.http.HttpMethod;

public enum RequestMethod {
    GET,
    POST,
    PUT,
    DELETE;

    public HttpMethod toHttpMethod() {
        return HttpMethod.valueOf(this.toString());
    }

}
