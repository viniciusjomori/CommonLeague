package br.com.jrr.apiTest.Ticket;

import java.util.Collection;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.jrr.apiTest.Request.HttpDTO;
import br.com.jrr.apiTest.Request.RequestService;
import br.com.jrr.apiTest.Request.Enum.RequestMethod;
import br.com.jrr.apiTest.Ticket.DTO.OrderRequestDTO;
import br.com.jrr.apiTest.Ticket.Entity.TicketEntity;
import br.com.jrr.apiTest.Ticket.Repository.TicketRepository;
import br.com.jrr.apiTest.User.UserEntity;
import br.com.jrr.apiTest.User.UserService;

@Service
public class TicketService {
    
    @Autowired
    private TicketRepository repository;

    @Autowired
    private UserService userService;

    @Autowired
    private RequestService requestService;

    @Value("cm-pay.base-endpoint")
    private String baseEndpoint;

    public Collection<TicketEntity> findAll() {
        return repository.findAll();
    }

    public TicketEntity findById(UUID id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public String orderTicket(TicketEntity ticket, int qnt) {
        UserEntity user = userService.getCurrentUser();
        OrderRequestDTO order = new OrderRequestDTO(
            user.getId().toString(),
            ticket.getId().toString(),
            ticket.getDescription(),
            qnt,
            ticket.getValue()
        );
        HttpDTO httpDTO = requestService.request("order", RequestMethod.POST, order);
        
        if(httpDTO.statusCode() == 200) 
            return httpDTO.jsonBody();

        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
