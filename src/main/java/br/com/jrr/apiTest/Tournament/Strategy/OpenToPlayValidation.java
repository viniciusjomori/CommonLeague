package br.com.jrr.apiTest.Tournament.Strategy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Component;

import br.com.jrr.apiTest.Team.Entity.TeamJoinEntity;

@Component
public class OpenToPlayValidation implements IMembersValidation {


    private List<String> usersCloseToPlay;

    @Override
    public boolean validate(Collection<TeamJoinEntity> members) {
        this.usersCloseToPlay = new ArrayList<>();
        for (TeamJoinEntity member : members) {
            
            if(!member.isOpenToPlay())
                this.usersCloseToPlay.add(member.getUser().getNickname());

        }
        return this.usersCloseToPlay.isEmpty();
    }

    @Override
    public String getMessage() {
        String users = String.join(", ", usersCloseToPlay);
        String message = usersCloseToPlay.size() > 1 
            ? "The following users are not open to play: " 
            : "The following user is not open to play: ";
        
        return message + users;
    }
    
}
