package br.com.jrr.apiTest.Tournament.Strategy;

import java.util.Collection;

import br.com.jrr.apiTest.Team.Entity.TeamJoinEntity;

public interface IMembersValidation {
    
    public boolean validate(Collection<TeamJoinEntity> members);

    public String getMessage();

}
