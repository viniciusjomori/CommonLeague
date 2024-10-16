package br.com.jrr.apiTest.domain.DTO;

import br.com.jrr.apiTest.domain.Account.AccountRiot;

import java.util.List;

public record DetailAccountDTO(
    String id,
    String puuid,
    String gameName,
    String tagLine,
    List<String> idMatchList
) {

    public DetailAccountDTO(AccountRiot accountRiot){
        this(accountRiot.getId(),  accountRiot.getPuuid(),  accountRiot.getGameName(),accountRiot.getTagLine() , accountRiot.getIdMatchList());
    }

}
