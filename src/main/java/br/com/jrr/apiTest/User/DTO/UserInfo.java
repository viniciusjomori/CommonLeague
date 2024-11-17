package br.com.jrr.apiTest.User.DTO;

import java.util.UUID;

public record UserInfo(
    UUID id,
    String nickname,
    String imagePath
) {
    
}
