package br.com.jrr.apiTest.Ticket.DTO;

import java.util.UUID;

public record TicketResponseDTO(
    UUID id,
    String description,
    double value
) {
    
}
