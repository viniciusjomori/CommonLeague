package br.com.jrr.apiTest.User.Enum;

import java.util.Arrays;

import br.com.jrr.apiTest.User.DTO.UserProfileDTO;

public enum UserProfile {
    RED("https://s2-techtudo.glbimg.com/c-EmZ-_KhZUSd3gExFL5aoecgHw=/0x0:695x471/984x0/smart/filters:strip_icc()/i.s3.glbimg.com/v1/AUTH_08fbf48bc0524877943fe86e43087e7a/internal_photos/bs/2021/R/j/rSa1InQW6QKu8AqlIZnw/2016-07-21-valor.jpg"),
    BLUE("https://s2-techtudo.glbimg.com/6A6W-4eM6NuN4z0SNYWQwDqn9pQ=/0x0:695x471/984x0/smart/filters:strip_icc()/i.s3.glbimg.com/v1/AUTH_08fbf48bc0524877943fe86e43087e7a/internal_photos/bs/2021/B/M/TY7zqmQuWkRvIlaVYhvA/2016-07-21-mystic.jpg"),
    YELLOW("https://s2-techtudo.glbimg.com/b3lvXthPOp3MHTId152Mnzcv-2o=/0x0:695x471/984x0/smart/filters:strip_icc()/i.s3.glbimg.com/v1/AUTH_08fbf48bc0524877943fe86e43087e7a/internal_photos/bs/2021/x/A/NbOyeqTD6jVPcfshidBQ/2016-07-21-instinct.jpg");

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
