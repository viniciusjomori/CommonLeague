package br.com.jrr.apiTest.domain.DTO;


import br.com.jrr.apiTest.domain.Account.AccountRiot;

public record AccountRiotDTO(
    String id,
    String puuid,
    String gameName,
    String tagLine,
    String accountId,
    String idRiot,
    String profileIconId,
    String revisionDate,
    String summonerLevel
) {
    public static AccountRiotDTO fromAccountRiot(AccountRiot accountRiot) {
        if (accountRiot == null) {
            // Retorne um DTO vazio ou lance uma exceção, dependendo do que é mais apropriado para o seu caso
            return new AccountRiotDTO(null,null,null,null,null,null,null,null, null);
        }

        return new AccountRiotDTO(
                accountRiot.getId(),
                accountRiot.getPuuid(),
                accountRiot.getGameName(),
                accountRiot.getTagLine(),
                accountRiot.getAccountId(),
                accountRiot.getIdRiot(),
                accountRiot.getProfileIconId(),
                accountRiot.getRevisionDate(),
                accountRiot.getSummonerLevel()
        );
    }
}