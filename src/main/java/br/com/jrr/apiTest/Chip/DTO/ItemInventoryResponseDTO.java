package br.com.jrr.apiTest.Chip.DTO;

import java.util.UUID;

public record ItemInventoryResponseDTO(
    UUID chipId,
    String description,
    int qnt,
    String imagePath
) {
    
}
