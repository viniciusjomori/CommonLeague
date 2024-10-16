package br.com.jrr.apiTest.Team.DTO;

import java.util.UUID;

public record TeamResponseDTO(
    UUID id,
    String name
) {
    
}
