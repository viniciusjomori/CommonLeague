package br.com.jrr.apiTest.RiotAccount.DTO;

import jakarta.validation.constraints.NotBlank;

public record RiotAccConnectDTO(
    @NotBlank String gameName,
    @NotBlank String tagLine
) {
    
}