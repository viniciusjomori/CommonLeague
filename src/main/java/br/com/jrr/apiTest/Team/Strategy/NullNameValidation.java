package br.com.jrr.apiTest.Team.Strategy;

import org.springframework.stereotype.Component;

import br.com.jrr.apiTest.Team.Entity.TeamEntity;

@Component
public class NullNameValidation implements ITeamValidation {

    @Override
    public boolean validate(TeamEntity team) {
        String name = team.getName();
        return name != null && !name.isEmpty();
    }

    @Override
    public String getMessage() {
        return "Name is null";
    }

    
}
