package br.com.jrr.apiTest.Tournament.Strategy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.jrr.apiTest.App.Exceptions.NotFoundException;
import br.com.jrr.apiTest.RiotAccount.RiotAccService;
import br.com.jrr.apiTest.Team.Entity.TeamJoinEntity;
import br.com.jrr.apiTest.User.UserEntity;

@Component
public class HasRiotAccValidation implements IMembersValidation {

    @Autowired
    private RiotAccService accountService;

    private List<String> usersWithoutAccount;

    @Override
    public boolean validate(Collection<TeamJoinEntity> members) {
        this.usersWithoutAccount = new ArrayList<>();
        for (TeamJoinEntity member : members) {
            UserEntity user = member.getUser();
            try {
                accountService.findByUser(user);
            } catch(NotFoundException ex) {
                this.usersWithoutAccount.add(user.getNickname());
            }

        }
        return this.usersWithoutAccount.isEmpty();
    }

    @Override
    public String getMessage() {
        String users = String.join(", ", usersWithoutAccount);
        String message = usersWithoutAccount.size() > 1 
            ? "The following users are not connected with a Riot Account: " 
            : "The following user is not connected with a Riot Account: ";
        
        return message + users;
    }
    
}
