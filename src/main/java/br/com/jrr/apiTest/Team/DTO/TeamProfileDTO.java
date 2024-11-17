package br.com.jrr.apiTest.Team.DTO;

import br.com.jrr.apiTest.Team.Enum.TeamProfile;

public record TeamProfileDTO(
    TeamProfile profile,
    String imagePath
) {
    
}
