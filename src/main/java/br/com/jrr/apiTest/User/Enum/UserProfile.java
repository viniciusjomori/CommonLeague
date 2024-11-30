package br.com.jrr.apiTest.User.Enum;

import java.util.Arrays;

import br.com.jrr.apiTest.User.DTO.UserProfileDTO;

public enum UserProfile {
    RED("https://github.com/NathanNevesPrates/CommonLeagueImages/blob/main/user/generic_user_red.jpeg?raw=true"),
    BLUE("https://github.com/NathanNevesPrates/CommonLeagueImages/blob/main/user/generic_user_blue.jpeg?raw=true"),
    YELLOW("https://github.com/NathanNevesPrates/CommonLeagueImages/blob/main/user/generic_user_yellow.jpeg?raw=true");

    public final String imagePath;

    UserProfile(String imagePath) {
        this.imagePath = imagePath;
    }

    public static UserProfileDTO[] toDTOs() {
        return Arrays.stream(UserProfile.values())
            .map(profile -> new UserProfileDTO(profile.name(), profile.imagePath))
            .toArray(UserProfileDTO[]::new);
    }
    
}
