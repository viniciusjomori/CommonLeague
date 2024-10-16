package br.com.jrr.apiTest.Ticket.Repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.jrr.apiTest.Ticket.Entity.TicketEntity;

public interface TicketRepository extends JpaRepository<TicketEntity, UUID> {
    
}
