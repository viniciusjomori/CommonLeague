package br.com.jrr.apiTest.Team.DTO;

import br.com.jrr.apiTest.Team.Enum.TeamJoinStatus;
import br.com.jrr.apiTest.Team.Enum.TeamRoleName;
import br.com.jrr.apiTest.User.DTO.UserInfo;

public record MemberResponseDTO(
    boolean openToPlay,
    TeamRoleName role,
    TeamJoinStatus status,
    UserInfo user
) {
    
}
