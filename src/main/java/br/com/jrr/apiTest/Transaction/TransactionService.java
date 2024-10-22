package br.com.jrr.apiTest.Transaction;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.jrr.apiTest.Chip.Service.InventoryService;
import br.com.jrr.apiTest.Transaction.Enum.TransactionStatus;

@Service
public class TransactionService {
    
    @Autowired
    private TransactionRepository repository;

    @Autowired
    private InventoryService inventoryService;

    public void processTransaction(UUID id) {
        TransactionEntity transaction = findById(id);
        TransactionStatus status = transaction.getStatus();
        if (status.equals(TransactionStatus.APPROVED))
            inventoryService.processTransaction(transaction);
    }

    private TransactionEntity findById(UUID id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
