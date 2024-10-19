package br.com.jrr.apiTest.Ticket.DTO;

import java.util.UUID;

public record ItemInventoryResponseDTO(
    UUID ticketId,
    String description,
    int qnt
) {
    
}
