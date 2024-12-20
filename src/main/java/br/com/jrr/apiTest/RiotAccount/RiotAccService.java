package br.com.jrr.apiTest.RiotAccount;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import br.com.jrr.apiTest.App.Exceptions.BadRequestException;
import br.com.jrr.apiTest.App.Exceptions.NotFoundException;
import br.com.jrr.apiTest.PyRequest.PyRequestService;
import br.com.jrr.apiTest.RiotAccount.DTO.RiotAccConnectDTO;
import br.com.jrr.apiTest.RiotAccount.DTO.RiotAccFromApiDTO;
import br.com.jrr.apiTest.RiotAccount.DTO.SummonerInfoDTO;
import br.com.jrr.apiTest.RiotRequest.RiotRequestService;
import br.com.jrr.apiTest.User.UserEntity;
import br.com.jrr.apiTest.User.UserService;

@Service
public class RiotAccService {
    
    @Autowired
    private UserService userService;

    @Autowired
    private RiotAccRepository repository;

    @Autowired
    private RiotRequestService riotRequestService;

    @Autowired
    private PyRequestService pyRequestService;
    

    public RiotAccEntity connect(RiotAccConnectDTO dto) {
        repository.findByPlayerInfo(dto.gameName(), dto.tagLine())
            .ifPresent(r -> { throw new BadRequestException("This Riot account is already connected"); });;

        RiotAccFromApiDTO riotAccResponse = riotRequestService.requestPuuid(dto.tagLine(), dto.gameName());
        

        return connect(riotAccResponse);
    }

    public RiotAccEntity connect(RiotAccFromApiDTO accInfo) {
        UserEntity user = userService.getCurrentUser();
        disconnect(user);

        RiotAccEntity entity = RiotAccEntity.builder()
            .user(user)
            .gameName(accInfo.gameName())
            .tagLine(accInfo.tagLine())
            .puuid(accInfo.puuid())
            .summonerLevel(0)
            .profileIconId(0)
            .build();
        
        entity = updateSummonerInfo(entity);
        
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

    protected RiotAccEntity updateSummonerInfo(RiotAccEntity account) {
        try {
            SummonerInfoDTO summonerInfo = pyRequestService.requestSummonerInfo(account.getPuuid());
            if(summonerInfo != null) {
                account.setSummonerLevel(summonerInfo.summonerLevel());
                account.setProfileIconId(summonerInfo.profileIconId());
            }
        } catch (WebClientRequestException ex) {}

        return account;
    }

}
