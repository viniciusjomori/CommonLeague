package br.com.jrr.apiTest.Tournament;

import java.util.UUID;

public record TournamentJoinResponseDTO(
    UUID id,
    int qntChipsPerPlayer
) {
    
}
