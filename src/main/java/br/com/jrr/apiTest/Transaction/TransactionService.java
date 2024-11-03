package br.com.jrr.apiTest.Transaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.jrr.apiTest.Chip.Entity.InventoryEntity;
import br.com.jrr.apiTest.Chip.Service.InventoryService;
import br.com.jrr.apiTest.Team.Entity.TeamJoinEntity;
import br.com.jrr.apiTest.Tournament.Entity.TournamentEntity;
import br.com.jrr.apiTest.Transaction.Enum.TransactionStatus;
import br.com.jrr.apiTest.Transaction.Enum.TransactionType;
import br.com.jrr.apiTest.User.UserEntity;

@Service
public class TransactionService {
    
    @Autowired
    private TransactionRepository repository;

    @Autowired
    private InventoryService inventoryService;

    public void processTransaction(UUID id) {
        TransactionEntity transaction = findById(id);
        inventoryService.processTransaction(transaction);
    }

    public void processTransaction(TransactionEntity transaction) {
        inventoryService.processTransaction(transaction);
    }

    public void processTransactions(Collection<TransactionEntity> transactions) {
        inventoryService.processTransactions(transactions);
    }

    public void createForTournament(Collection<TeamJoinEntity> members, int qntChips) {        
        Collection<InventoryEntity> inventories = findMembersInventories(members);

        for(InventoryEntity inventory : inventories) {
            if(inventory.getQnt() < qntChips)
                throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        
        createNewTransactions(inventories, qntChips, TransactionType.CREATE_TOURNAMENT);

    }

    public void refundTournament(Collection<TeamJoinEntity> members, TournamentEntity tournament) {
        Collection<InventoryEntity> inventories = findMembersInventories(members);
        createNewTransactions(inventories, tournament.getQntChipsPerPlayer(), TransactionType.REFUND_TOURNAMENT);
    }

    private Collection<InventoryEntity> findMembersInventories(Collection<TeamJoinEntity> members) {
        Collection<UserEntity> users = members.stream()
            .map(TeamJoinEntity::getUser)
            .collect(Collectors.toList());
        
        Collection<InventoryEntity> inventories = users.stream()
            .map(inventoryService::getCommomCoin)
            .collect(Collectors.toList());

        return inventories;
    }

    private TransactionEntity findById(UUID id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private void createNewTransactions(Collection<InventoryEntity> inventories, int qntChips, TransactionType type) {
        Collection<TransactionEntity> transactions = new ArrayList<>();
        for (InventoryEntity inventory : inventories) {
            TransactionEntity transaction = TransactionEntity.builder()
                .type(type)
                .status(TransactionStatus.pending)
                .qnt(qntChips)
                .inventory(inventory)
                .build();
            transactions.add(transaction);
        }
        repository.saveAll(transactions);
        processTransactions(transactions);
        
    }

}
