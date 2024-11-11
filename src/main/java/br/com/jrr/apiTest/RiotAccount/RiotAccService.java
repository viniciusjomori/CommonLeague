package br.com.jrr.apiTest.RiotAccount;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.google.gson.Gson;

import br.com.jrr.apiTest.App.Exceptions.NotFoundException;
import br.com.jrr.apiTest.Request.HttpDTO;
import br.com.jrr.apiTest.Request.Enum.RequestMethod;
import br.com.jrr.apiTest.Request.Service.RequestService;
import br.com.jrr.apiTest.RiotAccount.DTO.RiotAccConnectDTO;
import br.com.jrr.apiTest.RiotAccount.DTO.RiotAccFromApiDTO;
import br.com.jrr.apiTest.RiotAccount.Strategy.IRiotAccError;
import br.com.jrr.apiTest.User.UserEntity;
import br.com.jrr.apiTest.User.UserService;

@Service
public class RiotAccService {
    
    @Autowired
    private UserService userService;

    @Autowired
    private RiotAccRepository repository;

    @Autowired
    private RequestService requestService;
    
    @Autowired
    private Gson gson;

    @Autowired
    private List<IRiotAccError> errors;

    @Value("${lol.api-key}")
    private String apiKey;

    @Value("${lol.base-dns}")
    private String baseDns;

    public RiotAccEntity connect(RiotAccConnectDTO dto) {
        String endpoint = getEndpoint(dto.gameName(), dto.tagLine());

        HttpDTO httpDTO = requestService.request(
            endpoint,
            RequestMethod.GET
        );
        
        validateResponse(httpDTO);

        RiotAccFromApiDTO responseBody = gson.fromJson(httpDTO.jsonBody(), RiotAccFromApiDTO.class);
        return connect(responseBody);
    }

    public RiotAccEntity connect(RiotAccFromApiDTO dto) {
        UserEntity user = userService.getCurrentUser();
        disconnect(user);
        RiotAccEntity entity = RiotAccEntity.builder()
            .user(user)
            .gameName(dto.gameName())
            .tagLine(dto.tagLine())
            .puuid(dto.puuid())
            .build();
        
        return repository.save(entity);
    }

    public void disconnect(UserEntity user) {
        repository.findActiveByUser(user)
            .ifPresent(account -> {
                account.setActive(false);
                repository.save(account);
            });
    }

    public RiotAccEntity findByUser(UserEntity user) {
        return repository.findActiveByUser(user)
            .orElseThrow(() -> new NotFoundException("No Riot Account connected"));
    }

    public RiotAccEntity findCurrentAccount() {
        UserEntity user = userService.getCurrentUser();
        return findByUser(user);
    }

    public Collection<RiotAccEntity> findByUsers(Collection<UserEntity> users) {
        return repository.findActiveByUsers(users);
    }

    public Collection<RiotAccEntity> findByRiotIds(Collection<String> riotIds) {
        return repository.findActiveByRiotIds(riotIds);
    }

    private void validateResponse(HttpDTO dto) {
        if (dto.statusCode() == 200) return;
    
        IRiotAccError matchedError = errors.stream()
            .filter(error -> error.isError(dto))
            .findFirst()
            .orElse(null);
    
        int statusError = matchedError != null ? matchedError.getStatusCode() : 500;
        String errorMessage = matchedError != null ? matchedError.getMessage() : "Internal Server Error";
    
        throw new ResponseStatusException(HttpStatus.valueOf(statusError), errorMessage);
    }

    private String getEndpoint(String tagLine, String gameName) {
        StringBuilder sb = new StringBuilder();
        sb.append(baseDns);
        sb.append("lol/account/v1/accounts/by-riot-id/");
        sb.append(tagLine);
        sb.append("/");
        sb.append(gameName);
        sb.append("?api_key=");
        sb.append(apiKey);
        return sb.toString();
    }

}
