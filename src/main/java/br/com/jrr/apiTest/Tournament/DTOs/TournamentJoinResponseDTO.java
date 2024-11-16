package br.com.jrr.apiTest.Tournament.DTOs;

import java.util.UUID;

import br.com.jrr.apiTest.Team.DTO.TeamInfoDTO;
import br.com.jrr.apiTest.Tournament.Enum.TournamentJoinStatus;

public record TournamentJoinResponseDTO(
    UUID joinId,
    UUID tournamentId,
    TeamInfoDTO team,
    TournamentJoinStatus status,
    int qntChipsPerPlayer,
    int round
) {
    
}
