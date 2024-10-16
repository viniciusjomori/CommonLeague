package br.com.jrr.apiTest.App;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class ResponseDTO {
   private HttpStatusCode httpStatus;
   private String message;
}
