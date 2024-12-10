package br.com.jrr.apiTest.Chip.Controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jrr.apiTest.Chip.DTO.ItemInventoryResponseDTO;
import br.com.jrr.apiTest.Chip.Entity.InventoryEntity;
import br.com.jrr.apiTest.Chip.Mapper.InventoryMapper;
import br.com.jrr.apiTest.Chip.Service.InventoryService;
import jakarta.annotation.security.RolesAllowed;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService service;

    @Autowired
    private InventoryMapper mapper;
    
    @GetMapping
    @RolesAllowed("CLIENT")
    public ResponseEntity<Collection<ItemInventoryResponseDTO>> getInventory() {
        Collection<InventoryEntity> entities = service.findByCurrentUser();
        return ResponseEntity.ok(mapper.toResponse(entities));
    }

}
