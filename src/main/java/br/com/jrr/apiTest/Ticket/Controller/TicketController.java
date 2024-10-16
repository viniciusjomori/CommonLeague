package br.com.jrr.apiTest.Ticket.Controller;

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

import br.com.jrr.apiTest.Ticket.TicketService;
import br.com.jrr.apiTest.Ticket.DTO.TicketResponseDTO;
import br.com.jrr.apiTest.Ticket.Entity.TicketEntity;
import br.com.jrr.apiTest.Ticket.Mapper.TicketMapper;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/ticket")
public class TicketController {
    
    @Autowired
    private TicketService service;

    @Autowired
    private TicketMapper mapper;

    @GetMapping
    @PermitAll
    public ResponseEntity<Collection<TicketResponseDTO>> findAll() {
        Collection<TicketEntity> entities = service.findAll();
        return ResponseEntity.ok(mapper.toResponse(entities));
    }

    @PostMapping("/{id}/order/{qnt}")
    @RolesAllowed("CLIENT")
    public ResponseEntity<Void> orderTicket(@PathVariable UUID id, @PathVariable int qnt) {
        TicketEntity ticket = service.findById(id);
        service.orderTicket(ticket, qnt);
        return ResponseEntity.noContent().build();
    }

}
