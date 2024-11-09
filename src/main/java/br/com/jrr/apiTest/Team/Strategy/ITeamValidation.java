package br.com.jrr.apiTest.Team.Strategy;

import br.com.jrr.apiTest.Team.Entity.TeamEntity;

public interface ITeamValidation {
 
    public boolean validate(TeamEntity team);

    public String getMessage();

}
