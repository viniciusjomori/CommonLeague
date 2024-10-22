package br.com.jrr.apiTest.Chip.Service;

import java.util.Collection;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.google.gson.Gson;

import br.com.jrr.apiTest.Chip.DTO.BuyRequestDTO;
import br.com.jrr.apiTest.Chip.DTO.BuyResponseDTO;
import br.com.jrr.apiTest.Chip.Entity.InventoryEntity;
import br.com.jrr.apiTest.Chip.Entity.ChipEntity;
import br.com.jrr.apiTest.Chip.Repository.ChipRepository;
import br.com.jrr.apiTest.Request.HttpDTO;
import br.com.jrr.apiTest.Request.RequestService;
import br.com.jrr.apiTest.Request.Enum.RequestMethod;
@Service
public class ChipService {
    
    @Autowired
    private ChipRepository repository;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private RequestService requestService;

    @Autowired
    private Gson gson;

    @Value("commonleague-pay.base-endpoint")
    private String baseEndpoint;

    public Collection<ChipEntity> findAll() {
        return repository.findAll();
    }

    public ChipEntity findById(UUID id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public BuyResponseDTO buyChip(ChipEntity chip, int qnt) {
        InventoryEntity inventory= inventoryService.findByCurrentUser(chip);
        BuyRequestDTO order = new BuyRequestDTO(
            inventory.getId(),
            chip.getDescription(),
            qnt,
            chip.getValue()
        );
        HttpDTO httpDTO = requestService.request("buy", RequestMethod.POST, order);
        
        if(httpDTO.statusCode() == 200) {
            String json = httpDTO.jsonBody();
            BuyResponseDTO orderResponse = gson.fromJson(json, BuyResponseDTO.class);
            return orderResponse;
        }

        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
