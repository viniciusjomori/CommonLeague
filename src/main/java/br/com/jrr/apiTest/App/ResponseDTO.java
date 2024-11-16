package br.com.jrr.apiTest.App;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO {
   private HttpStatusCode httpStatus;
   private String message;
}
