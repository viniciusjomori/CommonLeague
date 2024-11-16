package br.com.jrr.apiTest.Transaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import br.com.jrr.apiTest.App.Exceptions.ConflictException;
import br.com.jrr.apiTest.App.Exceptions.NotFoundException;
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

    public void createForTournament(Collection<TeamJoinEntity> members, int qntChips, TournamentEntity tournament) {        
        Collection<InventoryEntity> inventories = findMembersInventories(members);

        for(InventoryEntity inventory : inventories) {
            if(inventory.getQnt() < qntChips)
                throw new ConflictException("Someone on your team doesn't have enough chips");
        }
        
        createNewTransactions(inventories, qntChips, TransactionType.CREATE_TOURNAMENT, tournament);

    }

    public void refundTournament(Collection<TeamJoinEntity> members, TournamentEntity tournament) {
        Collection<InventoryEntity> inventories = findMembersInventories(members);
        createNewTransactions(inventories, tournament.getQntChipsPerPlayer(), TransactionType.REFUND_TOURNAMENT);
    }

    public void rewardTournament(TournamentEntity tournament, Collection<TeamJoinEntity> winners, Collection<TeamJoinEntity> losers) {
        int qntChipPerPlayer = tournament.getQntChipsPerPlayer();
        int tournamentSize = losers.size() + winners.size();
        double amountChips = qntChipPerPlayer * tournamentSize * 0.85;

        int rewardPerPlayer = (int) Math.ceil(amountChips / winners.size());

        Collection<InventoryEntity> inventories = findMembersInventories(winners);
        createNewTransactions(inventories, rewardPerPlayer, TransactionType.WIN_TOURNAMENT);
    }

    public Page<TransactionEntity> findAllByCurrentUser(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Collection<InventoryEntity> inventories = inventoryService.findByCurrentUser();
        return repository.findAllByInventories(inventories, pageable);
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
            .orElseThrow(() -> new NotFoundException("Transaction not found"));
    }

    private void createNewTransactions(Collection<InventoryEntity> inventories, int qntChips, TransactionType type) {
        createNewTransactions(inventories, qntChips, type, null);
    }

    private void createNewTransactions(Collection<InventoryEntity> inventories, int qntChips, TransactionType type, TournamentEntity tournament) {
        Collection<TransactionEntity> transactions = new ArrayList<>();
        for (InventoryEntity inventory : inventories) {
            TransactionEntity transaction = TransactionEntity.builder()
                .type(type)
                .status(TransactionStatus.approved)
                .chipsQty(qntChips)
                .inventory(inventory)
                .tournament(tournament)
                .build();
            transactions.add(transaction);
        }
        repository.saveAll(transactions);
        processTransactions(transactions);
        
    }

}
