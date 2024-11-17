package br.com.jrr.apiTest.PyRequest.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PyBaseRequestDTO {
    private String user_key;
    private String method;
    private String puuid;
    private String match_id;

    public static PyBaseRequestDTO toRequestSummoner(String puuid) {
        PyBaseRequestDTO request = new PyBaseRequestDTO();
        request.setMethod("get_summoner_info");
        request.setPuuid(puuid);
        return request;
    }

    public static PyBaseRequestDTO toRequestMatch(String matchId) {
        PyBaseRequestDTO request = new PyBaseRequestDTO();
        request.setMethod("get_info_match");
        request.setMatch_id(matchId);
        return request;
    }
}
