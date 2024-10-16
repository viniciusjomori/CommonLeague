package br.com.jrr.apiTest.Team.DTO;

import java.util.UUID;

import br.com.jrr.apiTest.Team.Enum.TeamRoleName;

public record MemberResponseDTO(
    UUID userId,
    TeamRoleName role
) {
    
}
