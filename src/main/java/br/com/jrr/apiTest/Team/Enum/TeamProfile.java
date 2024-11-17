package br.com.jrr.apiTest.Team.Enum;

import java.util.ArrayList;
import java.util.Collection;

import br.com.jrr.apiTest.Team.DTO.TeamProfileDTO;

public enum TeamProfile {
    FISH("https://github.com/NathanNevesPrates/CommonLeagueImages/blob/main/team_icons/fish.png?raw=true"),
    PHOENIX("https://raw.githubusercontent.com/NathanNevesPrates/CommonLeagueImages/refs/heads/main/team_icons/phoenix.png"),
    TURTLE("https://github.com/NathanNevesPrates/CommonLeagueImages/blob/main/team_icons/turtle.png?raw=true");

    public String imagePath;

    private TeamProfile(String imagePath) {
        this.imagePath = imagePath;
    }

    public Collection<TeamProfileDTO> toDTOS() {
        Collection<TeamProfileDTO> dtos = new ArrayList<>();
        for (TeamProfile profile : TeamProfile.values()) {
            dtos.add(new TeamProfileDTO(profile, profile.imagePath));
        }
        return dtos;
    }
}
