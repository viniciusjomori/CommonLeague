package br.com.jrr.apiTest.PyRequest.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PyBaseRequestDTO {
    private String user_key;
    private String method;
    private String puuid;

    public static PyBaseRequestDTO toRequestSummoner(String puuid) {
        PyBaseRequestDTO request = new PyBaseRequestDTO();
        request.setMethod("get_summoner_info");
        request.setPuuid(puuid);
        return request;
    }
}
