package br.com.jrr.apiTest.RiotAccount.DTO;

import java.util.UUID;

public record RiotAccClientDTO(
    String gameName,
    String tagLine,
    UUID userId,
    int summonerLevel,
    int profileIconId
) {}