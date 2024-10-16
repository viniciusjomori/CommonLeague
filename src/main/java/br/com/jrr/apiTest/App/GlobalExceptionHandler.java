package br.com.jrr.apiTest.App;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private ResponseDTO responseDto;
    
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ResponseDTO> handleResponseStatusException(ResponseStatusException ex) {
        responseDto.setMessage(ex.getReason());
        responseDto.setHttpStatus(ex.getStatusCode());
        return new ResponseEntity<ResponseDTO>(responseDto, ex.getStatusCode());
    }

}
