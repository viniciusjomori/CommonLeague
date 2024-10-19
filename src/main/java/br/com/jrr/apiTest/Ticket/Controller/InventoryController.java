package br.com.jrr.apiTest.Ticket.Controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jrr.apiTest.Ticket.DTO.ItemInventoryResponseDTO;
import br.com.jrr.apiTest.Ticket.Entity.InventoryEntity;
import br.com.jrr.apiTest.Ticket.Mapper.InventoryMapper;
import br.com.jrr.apiTest.Ticket.Service.InventoryService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService service;

    @Autowired
    private InventoryMapper mapper;
    
    @GetMapping
    public ResponseEntity<Collection<ItemInventoryResponseDTO>> getInventory() {
        Collection<InventoryEntity> entities = service.findByCurrentUser();
        return ResponseEntity.ok(mapper.toResponse(entities));
    }

}
