package br.com.jrr.apiTest.Tournament.Strategy;

import java.util.Collection;

import org.springframework.stereotype.Component;

import br.com.jrr.apiTest.Team.Entity.TeamJoinEntity;

@Component
public class TeamSizeValidation implements IMembersValidation {

    @Override
    public boolean validate(Collection<TeamJoinEntity> members) {
        return members.size() == 5;
    }

    @Override
    public String getMessage() {
        return "Team size is not 5";
    }
    
}
