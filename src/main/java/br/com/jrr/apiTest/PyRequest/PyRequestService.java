package br.com.jrr.apiTest.PyRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import br.com.jrr.apiTest.PyRequest.DTOs.PyBaseRequestDTO;
import br.com.jrr.apiTest.Request.HttpDTO;
import br.com.jrr.apiTest.Request.Enum.RequestMethod;
import br.com.jrr.apiTest.Request.Service.RequestService;
import br.com.jrr.apiTest.RiotAccount.DTO.SummonerInfoDTO;

@Service
public class PyRequestService {
    
    @Autowired
    private RequestService requestService;

    @Autowired
    private Gson gson;

    @Value("${common-league-data.base-dns}")
    String baseDns;

    @Value("${common-league-data.password}")
    String password;

    public SummonerInfoDTO requestSummonerInfo(String puuid) {
        SummonerInfoDTO summonerInfo = null;

        PyBaseRequestDTO request = PyBaseRequestDTO.toRequestSummoner(puuid);
        HttpDTO response = request("riotrun", RequestMethod.GET, request);

        if(response.statusCode() == 200) {
            JsonObject jsonObject = gson.fromJson(response.jsonBody(), JsonObject.class);
            JsonObject dataObject = jsonObject.getAsJsonObject("data");
            summonerInfo = gson.fromJson(dataObject, SummonerInfoDTO.class);
        }

        return summonerInfo;
    }

    public String requestMatchMetaData(String riotId) {
        String metadata = null;
        PyBaseRequestDTO request = PyBaseRequestDTO.toRequestMatch(riotId);
        HttpDTO response = request("riotrun", RequestMethod.GET, request);

        if(response.statusCode() == 200) {
            JsonObject jsonObject = gson.fromJson(response.jsonBody(), JsonObject.class);
            JsonObject dataObject = jsonObject.getAsJsonObject("data");
            metadata = dataObject.toString();
        }

        return metadata;

    }

    private HttpDTO request(String endpoint, RequestMethod method, PyBaseRequestDTO body) {
        endpoint = baseDns + endpoint;
        body.setUser_key(password);
        return requestService.request(endpoint, method, body);
    }

}
