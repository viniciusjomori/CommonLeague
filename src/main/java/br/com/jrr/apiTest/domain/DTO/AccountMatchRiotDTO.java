package br.com.jrr.apiTest.domain.DTO;

import java.util.List;

public record AccountMatchRiotDTO (
     String id,
     String puuid,
     String gameName,
     String tagLine,
     List<String>idMatchList
) {}
