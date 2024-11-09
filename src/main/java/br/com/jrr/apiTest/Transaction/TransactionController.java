package br.com.jrr.apiTest.Transaction;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.jrr.apiTest.Request.Service.RequestSignatureService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("transaction")
public class TransactionController {
    
    @Autowired
    private TransactionService service;

    @Autowired
    private RequestSignatureService signatureService;

    @PostMapping("{id}/process")
    public ResponseEntity<Void> processTransaction(@RequestHeader HttpHeaders headers, @PathVariable UUID id) {
        
        String xSignature = headers.getFirst("x-signature");
        String xRequestId = headers.getFirst("x-request-id");
        
        boolean isValid = signatureService.validateClRequest(xSignature, xRequestId, id.toString());

        if (!isValid)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        
        service.processTransaction(id);
        return ResponseEntity.noContent().build();
    }

}
