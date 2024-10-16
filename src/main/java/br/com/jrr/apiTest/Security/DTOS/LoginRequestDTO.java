package br.com.jrr.apiTest.Security.DTOS;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
    @NotBlank String email,
    @NotBlank String password
) {
    
}
