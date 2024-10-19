package br.com.jrr.apiTest.Ticket.Service;

import java.util.Collection;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.google.gson.Gson;

import br.com.jrr.apiTest.Request.HttpDTO;
import br.com.jrr.apiTest.Request.RequestService;
import br.com.jrr.apiTest.Request.Enum.RequestMethod;
import br.com.jrr.apiTest.Ticket.DTO.BuyRequestDTO;
import br.com.jrr.apiTest.Ticket.DTO.BuyResponseDTO;
import br.com.jrr.apiTest.Ticket.Entity.InventoryEntity;
import br.com.jrr.apiTest.Ticket.Entity.TicketEntity;
import br.com.jrr.apiTest.Ticket.Repository.TicketRepository;
@Service
public class TicketService {
    
    @Autowired
    private TicketRepository repository;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private RequestService requestService;

    @Autowired
    private Gson gson;

    @Value("commonleague-pay.base-endpoint")
    private String baseEndpoint;

    public Collection<TicketEntity> findAll() {
        return repository.findAll();
    }

    public TicketEntity findById(UUID id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public BuyResponseDTO buyTicket(TicketEntity ticket, int qnt) {
        InventoryEntity inventory= inventoryService.findByCurrentUser(ticket);
        BuyRequestDTO order = new BuyRequestDTO(
            inventory.getId(),
            ticket.getDescription(),
            qnt,
            ticket.getValue()
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
