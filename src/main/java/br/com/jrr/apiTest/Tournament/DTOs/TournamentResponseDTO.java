package br.com.jrr.apiTest.Tournament.DTOs;

import java.util.UUID;

import br.com.jrr.apiTest.Team.DTO.TeamInfoDTO;
import br.com.jrr.apiTest.Tournament.Enum.TournamentStatus;

public record TournamentResponseDTO(
    UUID id,
    int qntChipsPerPlayer,
    TournamentStatus status,
    TeamInfoDTO winner
) {
    
}
