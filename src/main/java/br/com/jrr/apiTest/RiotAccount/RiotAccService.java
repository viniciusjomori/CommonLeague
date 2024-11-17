package br.com.jrr.apiTest.RiotAccount;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jrr.apiTest.App.Exceptions.BadRequestException;
import br.com.jrr.apiTest.App.Exceptions.NotFoundException;
import br.com.jrr.apiTest.RiotAccount.DTO.RiotAccConnectDTO;
import br.com.jrr.apiTest.RiotAccount.DTO.RiotAccFromApiDTO;
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
    

    public RiotAccEntity connect(RiotAccConnectDTO dto) {
        repository.findByPlayerInfo(dto.gameName(), dto.tagLine())
            .ifPresent(r -> { throw new BadRequestException("This Riot account is already connected"); });;

        RiotAccFromApiDTO response = riotRequestService.requestPuuid(dto.tagLine(), dto.gameName());
        
        return connect(response);
    }

    public RiotAccEntity connect(RiotAccFromApiDTO dto) {
        UserEntity user = userService.getCurrentUser();
        disconnect(user);
        RiotAccEntity entity = RiotAccEntity.builder()
            .user(user)
            .gameName(dto.gameName())
            .tagLine(dto.tagLine())
            .puuid(dto.puuid())
            .enableToChange(true)
            .build();
        
        return repository.save(entity);
    }

    public void disconnect(UserEntity user) {
        repository.findActiveByUser(user)
            .ifPresent(account -> {

                if (!account.isEnableToChange())
                    throw new BadRequestException("Your Riot Account are not enable to change");
                
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

    public Collection<RiotAccEntity> setEnableToChange(Collection<RiotAccEntity> accounts, boolean enableToChange) {
        for (RiotAccEntity account : accounts) {
            account.setEnableToChange(enableToChange);
        }
        return accounts;
    }

    

}
