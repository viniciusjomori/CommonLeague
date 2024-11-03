package br.com.jrr.apiTest.Chip.Controller;

import java.util.Collection;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jrr.apiTest.Chip.DTO.BuyResponseDTO;
import br.com.jrr.apiTest.Chip.DTO.ChipResponseDTO;
import br.com.jrr.apiTest.Chip.Entity.ChipEntity;
import br.com.jrr.apiTest.Chip.Mapper.ChipMapper;
import br.com.jrr.apiTest.Chip.Service.ChipService;
import br.com.jrr.apiTest.Chip.Service.InventoryService;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/chip")
public class ChipController {
    
    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ChipService chipService;

    @Autowired
    private ChipMapper mapper;

    @GetMapping
    @PermitAll
    public ResponseEntity<Collection<ChipResponseDTO>> findAll() {
        Collection<ChipEntity> entities = chipService.findAll();
        return ResponseEntity.ok(mapper.toResponse(entities));
    }

    @PostMapping("/{chipId}/buy/{qnt}")
    @RolesAllowed("CLIENT")
    public ResponseEntity<BuyResponseDTO> buyChip(@PathVariable UUID chipId, @PathVariable int qnt) {
        BuyResponseDTO order = inventoryService.buyChip(chipId, qnt);
        return ResponseEntity.ok(order);
    }

}
