package br.com.jrr.apiTest.Tournament.Listeners;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.jrr.apiTest.RiotAccount.RiotAccEntity;
import br.com.jrr.apiTest.RiotAccount.RiotAccService;
import br.com.jrr.apiTest.Team.Entity.TeamJoinEntity;
import br.com.jrr.apiTest.Team.Enum.TeamJoinStatus;
import br.com.jrr.apiTest.Team.Service.TeamService;
import br.com.jrr.apiTest.Tournament.Entity.TournamentJoinEntity;
import br.com.jrr.apiTest.User.UserEntity;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;

@Component
public class TournamentJoinListener {

    private static TeamService teamService;
    private static RiotAccService accountService;

    @Autowired
    public void init(TeamService teamService, RiotAccService accountService) {
        TournamentJoinListener.teamService = teamService;
        TournamentJoinListener.accountService = accountService;
    }

    @PrePersist
    public void lockRiotAccount(TournamentJoinEntity join) {
        Collection<TeamJoinEntity> members = teamService.findActiveByTeam(join.getTeam());
        Collection<UserEntity> users = members.stream()
            .map(TeamJoinEntity::getUser)
            .toList();
        Collection<RiotAccEntity> accounts = accountService.findByUsers(users);
        accountService.setEnableToChange(accounts, false);
    }

    @PostUpdate
    public void unlockRiotAccount(TournamentJoinEntity join) {
        if (join.getExitDate() != null) {
            Collection<UserEntity> users = join.getTeam()
                .getJoins()
                .stream()
                .filter(j -> j.getActive() && j.getJoinStatus().equals(TeamJoinStatus.ACTIVE))
                .map(TeamJoinEntity::getUser)
                .toList();
            
            Collection<RiotAccEntity> accounts = users.stream()
                .map(UserEntity::getRiotAccounts)
                .flatMap(Collection::stream)
                .filter(RiotAccEntity::getActive)
                .toList();
            
            accountService.setEnableToChange(accounts, true);
        }
    }
}
