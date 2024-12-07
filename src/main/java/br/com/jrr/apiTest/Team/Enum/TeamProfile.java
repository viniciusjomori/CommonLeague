package br.com.jrr.apiTest.Team.Enum;

import java.util.ArrayList;
import java.util.Collection;

import br.com.jrr.apiTest.Team.DTO.TeamProfileDTO;

public enum TeamProfile {
    FISH("https://raw.githubusercontent.com/NathanNevesPrates/CommonLeagueImages/refs/heads/main/team/fish.png"),
    PHOENIX("https://raw.githubusercontent.com/NathanNevesPrates/CommonLeagueImages/refs/heads/main/team/phoenix.png"),
    TURTLE("https://raw.githubusercontent.com/NathanNevesPrates/CommonLeagueImages/refs/heads/main/team/turtle.png");

    public String imagePath;

    private TeamProfile(String imagePath) {
        this.imagePath = imagePath;
    }

    public static Collection<TeamProfileDTO> toDTOS() {
        Collection<TeamProfileDTO> dtos = new ArrayList<>();
        for (TeamProfile profile : TeamProfile.values()) {
            dtos.add(new TeamProfileDTO(profile, profile.imagePath));
        }
        return dtos;
    }
}
