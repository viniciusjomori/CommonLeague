package br.com.jrr.apiTest.Chip.Service;

import java.util.Collection;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.jrr.apiTest.Chip.Entity.InventoryEntity;
import br.com.jrr.apiTest.Chip.Entity.ChipEntity;
import br.com.jrr.apiTest.Chip.Repository.InventoryRepository;
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

    public InventoryEntity findByCurrentUser(ChipEntity chip) {
        UserEntity user = userService.getCurrentUser();
        return repository.findByUser(user, chip)
            .orElseGet(() -> createEmptyInventory(user, chip));
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

    private InventoryEntity createEmptyInventory(UserEntity user, ChipEntity chip) {
        InventoryEntity inventory = InventoryEntity.builder()
            .user(user)
            .chip(chip)
            .qnt(0)
            .build();
        return repository.save(inventory);
    }

}
