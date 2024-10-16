package br.com.jrr.apiTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import br.com.jrr.apiTest.Team.DTO.TeamRegisterDTO;
import br.com.jrr.apiTest.Team.Entity.TeamEntity;
import br.com.jrr.apiTest.Team.Entity.TeamJoinEntity;
import br.com.jrr.apiTest.Team.Enum.TeamJoinStatus;
import br.com.jrr.apiTest.Team.Enum.TeamRoleName;
import br.com.jrr.apiTest.Team.Service.TeamService;
import jakarta.transaction.Transactional;

@SpringBootTest
public class TeamTest extends BaseTest {
    
    @Autowired
    private TeamService teamService;

    TeamEntity team;

    TeamJoinEntity invite2;
    TeamJoinEntity invite3;
    TeamJoinEntity invite4;
    TeamJoinEntity invite5;
    TeamJoinEntity invite6;

    @BeforeEach
    public void setupUser() {
        defineCurrentUser(user1);

        TeamRegisterDTO register = new TeamRegisterDTO("team name test");
        team = teamService.registerTeam(register);

        invite2 = teamService.inviteUser(user2);
        invite3 = teamService.inviteUser(user3);
        invite4 = teamService.inviteUser(user4);
        invite5 = teamService.inviteUser(user5);
    }

    @Test
    @Transactional
    public void registerTeam_Succcess() {

        TeamJoinEntity join = teamService.findActiveByUser(user1).get();
        
        assertEquals(join.getTeam(), team);
        assertEquals(join.getUser().getId(), user1.getId());
        assertEquals(join.getRole(), TeamRoleName.ROLE_TEAM_CAPTAIN);
        assertEquals(join.getJoinStatus(), TeamJoinStatus.ACTIVE);
        
    }

    @Test
    @Transactional
    public void registerTeam_AlreadyInTeam_Fail() {
        TeamRegisterDTO register = new TeamRegisterDTO("team name test 2");

        assertThrows(ResponseStatusException.class, () -> {
            teamService.registerTeam(register);
        });
    }

    @Test
    @Transactional
    public void leftTeam_Success() {

        teamService.leftTeam();

        assertDoesNotThrow(() -> {
            TeamRegisterDTO newRegister = new TeamRegisterDTO("new team name test");
            teamService.registerTeam(newRegister);
        });
    }

    @Test
    @Transactional
    public void inviteUser_TeamDontAcceptNewMembers_Fail() {
        
        defineCurrentUser(user2);
        teamService.acceptInvite(invite2.getId());

        defineCurrentUser(user3);
        teamService.acceptInvite(invite3.getId());

        defineCurrentUser(user4);
        teamService.acceptInvite(invite4.getId());

        defineCurrentUser(user5);
        teamService.acceptInvite(invite5.getId());

        assertThrows(ResponseStatusException.class, () -> {
            teamService.inviteUser(user6);
        });

    }

    @Test
    @Transactional
    public void interactInvite_IsNotForYou_Fail() {
        assertThrows(ResponseStatusException.class, () -> {
            teamService.acceptInvite(invite2.getId());
        });
        assertThrows(ResponseStatusException.class, () -> {
            teamService.refuseInvite(invite2.getId());
        });
    }

    @Test
    @Transactional
    public void interactInvite_IsNotPending_Fail() {
        defineCurrentUser(user2);
        teamService.acceptInvite(invite2.getId());

        assertThrows(ResponseStatusException.class, () -> {
            teamService.acceptInvite(invite2.getId());
        });
        assertThrows(ResponseStatusException.class, () -> {
            teamService.refuseInvite(invite2.getId());
        });
    }

    @Test
    @Transactional
    public void acceptInvite_Success() {

        defineCurrentUser(user2);
        teamService.acceptInvite(invite2.getId());

        TeamJoinEntity join = teamService.findActiveByUser(user2)
            .get();
        
        assertEquals(team, join.getTeam());

    }

    @Test
    @Transactional
    public void acceptInvite_AlreadyInTeam_Fail() {
        defineCurrentUser(user2);

        teamService.registerTeam(new TeamRegisterDTO("already in team"));

        assertThrows(ResponseStatusException.class, () -> {
            teamService.acceptInvite(invite2.getId());
        });
    }

    @Test
    @Transactional
    public void acceptInvite_TeamDontAcceptNewMembers_Fail() {
        TeamJoinEntity invite6 = teamService.inviteUser(user6);
        
        defineCurrentUser(user2);
        teamService.acceptInvite(invite2.getId());

        defineCurrentUser(user3);
        teamService.acceptInvite(invite3.getId());

        defineCurrentUser(user4);
        teamService.acceptInvite(invite4.getId());

        defineCurrentUser(user5);
        teamService.acceptInvite(invite5.getId());

        assertThrows(ResponseStatusException.class, () -> {
            defineCurrentUser(user6);
            teamService.acceptInvite(invite6.getId());
        });
        
    }

    @Test
    @Transactional
    public void refuseInvite_Success() {
        defineCurrentUser(user2);
        teamService.refuseInvite(invite2.getId());

        assertTrue(teamService.findActiveByUser(user2).isEmpty());

    }

    @Test
    @Transactional
    public void banUser_Success() {    
        defineCurrentUser(user2);
        teamService.acceptInvite(invite2.getId());

        defineCurrentUser(user1);
        teamService.banUser(user2);

        assertTrue(teamService.findActiveByUser(user2).isEmpty());
    }
    
    @Test
    @Transactional
    public void leftTeam_randomizeNewCap_Success() {

        defineCurrentUser(user2);
        teamService.acceptInvite(invite2.getId());

        defineCurrentUser(user3);
        teamService.acceptInvite(invite3.getId());

        defineCurrentUser(user4);
        teamService.acceptInvite(invite4.getId());

        defineCurrentUser(user5);
        teamService.acceptInvite(invite5.getId());

        defineCurrentUser(user1);
        teamService.leftTeam();

        Collection<TeamJoinEntity> joins = teamService.findActiveByTeam(team);
        long qntCap = joins.stream()
            .filter(join -> join.getRole() == TeamRoleName.ROLE_TEAM_CAPTAIN)
            .count();

        assertEquals(qntCap, 1);
        assertEquals(joins.size(), 4);

    }

    @Test
    @Transactional
    public void banUser_IsNotInTeam_Fail() {
        assertThrows(ResponseStatusException.class, () -> {
            teamService.banUser(user2);
        });
    }

    @Test
    @Transactional
    public void banUser_AutoBan_Fail() {
        assertThrows(ResponseStatusException.class, () -> {
            teamService.banUser(user1);
        });
        
    }

    @Test
    @Transactional
    public void toCaptain_Success() {
        defineCurrentUser(user2);
        teamService.acceptInvite(invite2.getId());

        defineCurrentUser(user1);
        teamService.toCaptain(user2);

        TeamJoinEntity join1 = teamService.findActiveByUser(user1)
            .get();
        TeamJoinEntity join2 = teamService.findActiveByUser(user2)
            .get();
        
        assertEquals(join1.getRole(), TeamRoleName.ROLE_TEAM_MEMBER);
        assertEquals(join2.getRole(), TeamRoleName.ROLE_TEAM_CAPTAIN);
        
    }

    @Test
    @Transactional
    public void toCaptain_SameUser_Fail() {
        assertThrows(ResponseStatusException.class, () -> {
            teamService.toCaptain(user1);
        });
    }

    @Test
    @Transactional
    public void toCaptain_IsNotInTeam_Fail() {
        assertThrows(ResponseStatusException.class, () -> {
            teamService.toCaptain(user2);
        });
    }

}
