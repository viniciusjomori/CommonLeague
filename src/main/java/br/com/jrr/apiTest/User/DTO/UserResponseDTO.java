package br.com.jrr.apiTest.User.DTO;

import java.time.LocalDate;

import java.util.UUID;

public record UserResponseDTO(
    UUID id,
    String nickname,
    String email,
    String fullName,
    LocalDate birthday
) {}