package br.com.jrr.apiTest.domain.API;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


@JsonIgnoreProperties(ignoreUnknown = true)
public record DataAccountAPI(String id,
    @NotBlank
    @JsonAlias("puuid")String puuid,
    @JsonAlias("gameName")String gameName,
    @JsonAlias("tagLine")String tagLine,

    @JsonAlias("accountId")String accountId,
    @JsonAlias("id")String idRiot,
    @JsonAlias("revisionDate")String revisionDate,

    @JsonAlias("profileIconId")String profileIconId,
    @JsonAlias("profileIconId")String summonerLevel

){

}
