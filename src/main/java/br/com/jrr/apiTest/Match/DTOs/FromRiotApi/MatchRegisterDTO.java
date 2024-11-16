package br.com.jrr.apiTest.Match.DTOs.FromRiotApi;

public record MatchRegisterDTO(
    MatchMetaDataDTO metadata,
    MatchInfoDTO info
) {}
