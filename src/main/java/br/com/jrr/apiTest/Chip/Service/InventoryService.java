package br.com.jrr.apiTest.Chip.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.google.gson.Gson;

import br.com.jrr.apiTest.Chip.Entity.InventoryEntity;
import br.com.jrr.apiTest.Chip.DTO.BuyRequestDTO;
import br.com.jrr.apiTest.Chip.DTO.BuyResponseDTO;
import br.com.jrr.apiTest.Chip.Entity.ChipEntity;
import br.com.jrr.apiTest.Chip.Repository.InventoryRepository;
import br.com.jrr.apiTest.Request.HttpDTO;
import br.com.jrr.apiTest.Request.Enum.RequestMethod;
import br.com.jrr.apiTest.Request.Service.RequestService;
import br.com.jrr.apiTest.Request.Service.RequestSignatureService;
import br.com.jrr.apiTest.Transaction.TransactionEntity;
import br.com.jrr.apiTest.User.UserEntity;
import br.com.jrr.apiTest.User.UserService;

@Service
public class InventoryService {
    
    @Autowired
    private InventoryRepository repository;

    @Autowired
    private UserService userService;

    @Autowired
    private ChipService chipService;

    @Autowired
    private RequestService requestService;

    @Autowired
    private Gson gson;

    @Autowired
    private RequestSignatureService signatureService;

    @Value("${commonleague-pay.base-dns}")
    private String baseEndpoint;

    public Collection<InventoryEntity> findByCurrentUser() {
        UserEntity user = userService.getCurrentUser();
        return repository.findByUser(user);
    }

    public BuyResponseDTO buyChip(UUID chipId, int qnt) {
        ChipEntity chip = chipService.findById(chipId);
        InventoryEntity inventory = findByCurrentUser(chip);
        BuyRequestDTO order = new BuyRequestDTO(
            inventory.getId(),
            chip.getDescription(),
            qnt,
            chip.getValue()
        );
        String endpoint = baseEndpoint + "buychips";

        Map<String, String> headers = new HashMap<>();

        String xRequestId = UUID.randomUUID().toString();
        String xSignature = signatureService.generateSignature(
            xRequestId,
            inventory.getId().toString()
        );

        headers.put("x-signature", xSignature);
        headers.put("x-request-id", xRequestId);

        HttpDTO httpDTO = requestService.request(endpoint, RequestMethod.POST, order, headers);
        
        if(httpDTO.statusCode() == 200) {
            String json = httpDTO.jsonBody();
            BuyResponseDTO orderResponse = gson.fromJson(json, BuyResponseDTO.class);
            return orderResponse;
        }

        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public InventoryEntity findByUser(UserEntity user, ChipEntity chip) {
        return repository.findByUser(user, chip)
            .orElseGet(() -> createEmptyInventory(user, chip));
    }

    public InventoryEntity findByCurrentUser(ChipEntity chip) {
        UserEntity user = userService.getCurrentUser();
        return findByUser(user, chip);
    }

    public InventoryEntity getCommomCoin(UserEntity user) {
        ChipEntity commomCoin = chipService.getCommomCoin();
        return findByUser(user, commomCoin);
    }

    public InventoryEntity findById(UUID id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public InventoryEntity processTransaction(TransactionEntity transaction) {
        InventoryEntity inventory = calculateInventory(transaction);
        return repository.save(inventory);
    }

    public Collection<InventoryEntity> processTransactions(Collection<TransactionEntity> transactions) {
        Collection<InventoryEntity> inventories = new ArrayList<>();
        for (TransactionEntity transaction : transactions) {
            InventoryEntity inventory = calculateInventory(transaction);
            inventories.add(inventory);
        }

        return repository.saveAll(inventories);
    }

    private InventoryEntity calculateInventory(TransactionEntity transaction) {
        InventoryEntity inventory = transaction.getInventory();
        
        boolean plus = transaction.getType().plus;
        int operation = plus ? 1 : -1;
        int qnt = transaction.getChipsQty() * operation;

        inventory.plusQnt(qnt);
        if (inventory.getQnt() < 0)
            throw new ResponseStatusException(HttpStatus.CONFLICT);
            
        return inventory;
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
