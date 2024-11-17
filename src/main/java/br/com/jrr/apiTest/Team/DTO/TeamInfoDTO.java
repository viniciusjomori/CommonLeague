package br.com.jrr.apiTest.Team.DTO;

import java.util.UUID;

public record TeamInfoDTO(
    UUID id,
    String name,
    String imagePath
) {
    
}
