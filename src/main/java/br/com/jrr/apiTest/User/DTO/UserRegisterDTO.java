package br.com.jrr.apiTest.User.DTO;

import java.time.LocalDate;

import br.com.jrr.apiTest.User.Enum.UserProfile;

public record UserRegisterDTO(

    String nickname,
    String email,
    String fullName,
    String cpf,
    LocalDate birthday,
    String password,
    UserProfile profile

) {}
