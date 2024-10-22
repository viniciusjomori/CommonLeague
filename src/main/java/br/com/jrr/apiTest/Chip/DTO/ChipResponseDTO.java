package br.com.jrr.apiTest.Chip.DTO;

import java.util.UUID;

public record ChipResponseDTO(
    UUID id,
    String description,
    double value
) {
    
}
