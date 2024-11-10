package br.com.jrr.apiTest.Tournament.DTOs;

import java.util.UUID;

public record TournamentJoinResponseDTO(
    UUID id,
    int qntChipsPerPlayer
) {
    
}
