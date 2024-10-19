package br.com.jrr.apiTest.Transaction;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("transaction")
public class TransactionController {
    
    @Autowired
    private TransactionService service;

    @PostMapping("{id}/process")
    public ResponseEntity<Void> processTransaction(@PathVariable UUID id) {
        service.processTransaction(id);
        return ResponseEntity.noContent().build();
    }

}
