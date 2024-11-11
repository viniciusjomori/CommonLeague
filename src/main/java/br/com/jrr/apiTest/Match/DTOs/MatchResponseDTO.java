package br.com.jrr.apiTest.Match.DTOs;

import java.util.UUID;

import br.com.jrr.apiTest.Match.Enums.MatchStatus;

public record MatchResponseDTO(
    UUID id,
    String riotId,
    UUID tournamentId,
    MatchStatus status
    
) {
    
}