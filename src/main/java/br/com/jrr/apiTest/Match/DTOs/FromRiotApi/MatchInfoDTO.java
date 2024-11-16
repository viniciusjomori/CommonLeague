package br.com.jrr.apiTest.Match.DTOs.FromRiotApi;

import java.util.List;

public record MatchInfoDTO(
    List<MatchParticipantDTO> participants,
    List<MatchTeamDTO> teams,
    String endOfGameResult
) {}
