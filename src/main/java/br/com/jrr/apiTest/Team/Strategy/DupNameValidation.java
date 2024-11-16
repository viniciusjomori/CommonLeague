package br.com.jrr.apiTest.Team.Strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.jrr.apiTest.Team.Entity.TeamEntity;
import br.com.jrr.apiTest.Team.Repository.TeamRepository;

@Component
public class DupNameValidation implements ITeamValidation {

    @Autowired
    private TeamRepository repository;

    @Override
    public boolean validate(TeamEntity team) {
        return repository.findByName(team.getName())
            .filter(existingTeam -> !existingTeam.getId().equals(team.getId()))
            .isEmpty();
    }

    @Override
    public String getMessage() {
        return "Name already exists";
    }
    
}
