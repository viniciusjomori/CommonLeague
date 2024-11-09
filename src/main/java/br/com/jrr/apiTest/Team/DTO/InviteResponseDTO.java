package br.com.jrr.apiTest.Team.DTO;

import java.util.UUID;

import br.com.jrr.apiTest.Team.Enum.TeamJoinStatus;

public record InviteResponseDTO(
    UUID inviteId,
    UUID teamId,
    UUID userId,
    TeamJoinStatus status
) {
    
}
