package br.com.jrr.apiTest.Ticket.Service;

import java.util.Collection;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.jrr.apiTest.Ticket.Entity.InventoryEntity;
import br.com.jrr.apiTest.Ticket.Entity.TicketEntity;
import br.com.jrr.apiTest.Ticket.Repository.InventoryRepository;
import br.com.jrr.apiTest.Transaction.TransactionEntity;
import br.com.jrr.apiTest.User.UserEntity;
import br.com.jrr.apiTest.User.UserService;

@Service
public class InventoryService {
    
    @Autowired
    private InventoryRepository repository;

    @Autowired
    private UserService userService;

    public Collection<InventoryEntity> findByCurrentUser() {
        UserEntity user = userService.getCurrentUser();
        return repository.findByUser(user);
    }

    public InventoryEntity findByCurrentUser(TicketEntity ticket) {
        UserEntity user = userService.getCurrentUser();
        return repository.findByUser(user, ticket)
            .orElseGet(() -> createEmptyInventory(user, ticket));
    }

    public InventoryEntity findById(UUID id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public InventoryEntity processTransaction(TransactionEntity transaction) {
        InventoryEntity inventory = transaction.getInventory();
        
        boolean plus = transaction.getType().plus;
        int operation = plus ? 1 : -1;
        int qnt = transaction.getQnt() * operation;

        inventory.plusQnt(qnt);
        return repository.save(inventory);
    }

    private InventoryEntity createEmptyInventory(UserEntity user, TicketEntity ticket) {
        InventoryEntity inventory = InventoryEntity.builder()
            .user(user)
            .ticket(ticket)
            .qnt(0)
            .build();
        return repository.save(inventory);
    }

}
