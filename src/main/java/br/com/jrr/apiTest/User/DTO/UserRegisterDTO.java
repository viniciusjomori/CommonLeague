package br.com.jrr.apiTest.User.DTO;

import java.time.LocalDate;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRegisterDTO(

    @NotBlank
    String nickname,

    @NotBlank
    @Email
    String email,

    @NotBlank
    String fullName,

    @NotBlank
    @CPF
    String cpf,
    
    @NotNull
    LocalDate birthday,

    @NotBlank
    String password

) {}
