package br.com.jrr.apiTest.Tournament.DTOs;

import java.util.Collection;

public record TournamentCodeRegisterDTO(
    Collection<String> allowedParticipants,
    String metadata,
    int teamSize,
    String pickType,
    boolean enoughPlayers,
    String spectatorType,
    String mapType
) {
    
}
