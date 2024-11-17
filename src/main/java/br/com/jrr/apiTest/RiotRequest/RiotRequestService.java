package br.com.jrr.apiTest.RiotRequest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.google.gson.Gson;

import br.com.jrr.apiTest.App.Exceptions.InternalServerErrorException;
import br.com.jrr.apiTest.Request.HttpDTO;
import br.com.jrr.apiTest.Request.Enum.RequestMethod;
import br.com.jrr.apiTest.Request.Service.RequestService;
import br.com.jrr.apiTest.RiotAccount.DTO.RiotAccFromApiDTO;
import br.com.jrr.apiTest.RiotAccount.Strategy.IRiotAccError;
import br.com.jrr.apiTest.Tournament.DTOs.RiotAPI.ProviderRequestDTO;
import br.com.jrr.apiTest.Tournament.DTOs.RiotAPI.TournamentCodeRegisterDTO;
import br.com.jrr.apiTest.Tournament.DTOs.RiotAPI.TournamentConnectDTO;
import br.com.jrr.apiTest.Tournament.Entity.TournamentEntity;

@Service
public class RiotRequestService {
    
    @Autowired
    private RiotApiKeyRepository repository;

    @Autowired
    private RequestService requestService;

    @Value("${lol.base-dns}")
    private String baseDns;

    @Autowired
    private Gson gson;

    @Autowired
    private List<IRiotAccError> errors;

    public void setApiKey(String apiKey) {
        desactiveKey();
        RiotApiKeyEntity entity = new RiotApiKeyEntity();
        entity.setValue(apiKey);
        repository.save(entity);
    }

    public RiotAccFromApiDTO requestPuuid(String tagLine, String gameName) {
        String endpoint = String.format(
            "riot/account/v1/accounts/by-riot-id/%s/%s",
            gameName, tagLine
        );
        HttpDTO response = request(endpoint, RequestMethod.GET);

        return toRiotAcc(response);
    }

    public int requestProviderId(TournamentEntity tournament) {
        ProviderRequestDTO request = new ProviderRequestDTO(
            "BR", 
            "http://localhost:8080/match?tournament=" + tournament.getId().toString() + "?"
        );

        String endpoint = "lol/tournament-stub/v5/providers";
        HttpDTO response = request(endpoint, RequestMethod.POST, request);

        if(response.statusCode() == 200)
            return Integer.parseInt(response.jsonBody());
        
        throw new InternalServerErrorException();
    }

    public String requestTournament(TournamentEntity entity, int providerId) {
        TournamentConnectDTO request = new TournamentConnectDTO(entity.getId().toString(), providerId);
        
        String endpoint = "lol/tournament-stub/v5/tournaments";
        HttpDTO response = request(endpoint, RequestMethod.POST, request);

        if(response.statusCode() == 200)
            return response.jsonBody();
        
        throw new InternalServerErrorException();
    }

    public String requestTournamentCode(TournamentCodeRegisterDTO request, int numberOfMatches, int riotId) {
        String endpoint = "lol/tournament-stub/v5/codes?count=" + numberOfMatches + "&tournamentId=" + riotId;

        HttpDTO response = request(endpoint, RequestMethod.POST, request);

        if(response.statusCode() == 200) {
            return response.jsonBody();
        }

        throw new InternalServerErrorException();

    }

    private void desactiveKey() {
        repository.findActive()
            .ifPresent(k -> {
                k.setActive(false);
                repository.save(k);
            });
    }

    private HttpDTO request(String endpoint, RequestMethod method, Object body) {
        String and = !endpoint.contains("?") ? "?" : "&";

        String apiKey = repository.findActive()
            .orElseThrow(() -> new InternalServerErrorException())
            .getValue();
        endpoint = baseDns + endpoint + and + "api_key=" + apiKey;

        return requestService.request(endpoint, method, body);
    }

    private HttpDTO request(String endpoint, RequestMethod method) {
        return request(endpoint, method, null);
    }

    private RiotAccFromApiDTO toRiotAcc(HttpDTO dto) {
        if (dto.statusCode() == 200) 
            return gson.fromJson(dto.jsonBody(), RiotAccFromApiDTO.class);
    
        IRiotAccError matchedError = errors.stream()
            .filter(error -> error.isError(dto))
            .findFirst()
            .orElse(null);
    
        int statusError = matchedError != null ? matchedError.getStatusCode() : 500;
        String errorMessage = matchedError != null ? matchedError.getMessage() : "Internal Server Error";
    
        throw new ResponseStatusException(HttpStatus.valueOf(statusError), errorMessage);
    }
}
