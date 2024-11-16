package br.com.jrr.apiTest.Transaction;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.jrr.apiTest.App.ResponseDTO;
import br.com.jrr.apiTest.Request.Service.RequestSignatureService;
import br.com.jrr.apiTest.Transaction.DTOs.TransactionResponseDTO;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("transaction")
public class TransactionController {
    
    @Autowired
    private TransactionService service;

    @Autowired
    private TransactionMapper mapper;

    @Autowired
    private RequestSignatureService signatureService;

    @PostMapping("{id}/process")
    public ResponseEntity<ResponseDTO> processTransaction(@RequestHeader HttpHeaders headers, @PathVariable UUID id) {
        
        String xSignature = headers.getFirst("x-signature");
        String xRequestId = headers.getFirst("x-request-id");
        
        boolean isValid = signatureService.validateClRequest(xSignature, xRequestId, id.toString());

        if (!isValid)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        
        service.processTransaction(id);

        ResponseDTO response = new ResponseDTO(HttpStatus.OK, "Transactions processed succesfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<TransactionResponseDTO>> findAllByCurrentUser(
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Page<TransactionEntity> entities = service.findAllByCurrentUser(page, size);
        return ResponseEntity.ok(mapper.toResponse(entities));
    }

}
