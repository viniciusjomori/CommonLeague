package br.com.jrr.apiTest.domain.DTO;

import java.util.List;

public record MatchDTO(
    String matchId,
    String gameMode,
    List<String> participants,
    String endOfGameResult
) {}
