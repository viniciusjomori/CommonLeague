package br.com.jrr.apiTest.User.DTO;

import br.com.jrr.apiTest.User.Enum.UserProfile;

public record UserUpdateDTO(
    String nickname,
    UserProfile profile,
    String pixKey,
    String password
) {
    
}
