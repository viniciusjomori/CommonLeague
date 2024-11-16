package br.com.jrr.apiTest.Chip.DTO;

import java.util.UUID;

public record SaleClientRequestDTO(
    UUID chipId,
    int qnt,
    String password
) {
    
}
