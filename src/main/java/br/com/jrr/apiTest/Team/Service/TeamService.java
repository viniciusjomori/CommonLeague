package br.com.jrr.apiTest.Team.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.jrr.apiTest.App.Exceptions.BadRequestException;
import br.com.jrr.apiTest.App.Exceptions.NotFoundException;
import br.com.jrr.apiTest.RiotAccount.RiotAccEntity;
import br.com.jrr.apiTest.RiotAccount.RiotAccService;
import br.com.jrr.apiTest.Team.DTO.TeamRegisterDTO;
import br.com.jrr.apiTest.Team.Entity.TeamEntity;
import br.com.jrr.apiTest.Team.Entity.TeamJoinEntity;
import br.com.jrr.apiTest.Team.Enum.TeamJoinStatus;
import br.com.jrr.apiTest.Team.Enum.TeamRoleName;
import br.com.jrr.apiTest.Team.Mapper.TeamMapper;
import br.com.jrr.apiTest.Team.Repository.TeamJoinRepository;
import br.com.jrr.apiTest.Team.Repository.TeamRepository;
import br.com.jrr.apiTest.Team.Strategy.ITeamValidation;
import br.com.jrr.apiTest.User.UserEntity;
import br.com.jrr.apiTest.User.UserService;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamJoinRepository joinRepository;

    @Autowired
    private TeamMapper teamMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private RiotAccService riotService;

    @Autowired
    private List<ITeamValidation> validations;

    public TeamEntity registerTeam(TeamRegisterDTO dto) {
        UserEntity user = userService.getCurrentUser();
        findActiveByUser(user).ifPresent(join -> {
            throw new BadRequestException("Already in team");
        });

        TeamEntity newTeam = teamMapper.toEntity(dto);
        newTeam = saveTeam(newTeam);

        TeamJoinEntity newJoin = TeamJoinEntity.builder()
            .team(newTeam)
            .user(user)
            .joinStatus(TeamJoinStatus.ACTIVE)
            .role(TeamRoleName.ROLE_TEAM_CAPTAIN)
            .responseDate(LocalDateTime.now())
            .build();

        joinRepository.save(newJoin);

        return newTeam;
    }

    public Optional<TeamJoinEntity> findActiveByUser(UserEntity user) {
        return joinRepository.findActiveByUser(user);
    }

    public Collection<TeamJoinEntity> findPendingByCurrentUser(){
        return joinRepository.findPendingByUser(userService.getCurrentUser());
    }

    public Collection<TeamJoinEntity> findActiveByTeam(TeamEntity team) {
        return joinRepository.findActiveByTeam(team);
    }

    public Collection<TeamJoinEntity> findActiveByUsers(Collection<UserEntity> users) {
        return joinRepository.findActiveByUsers(users);
    }

    public Optional<TeamJoinEntity> getCurrentTeam() {
        UserEntity user = userService.getCurrentUser();
        return findActiveByUser(user);
    }

    public TeamJoinEntity leftTeam() {
        UserEntity user = userService.getCurrentUser();
        TeamJoinEntity join = findActiveByUser(user)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

        join.setJoinStatus(TeamJoinStatus.LEFT);
        join.setActive(false);
        join = joinRepository.save(join);

        if(join.getRole().equals(TeamRoleName.ROLE_TEAM_CAPTAIN))
            randomizeNewCaptain(join.getTeam());

        return join;
    }

    public TeamJoinEntity inviteUser(UserEntity user) {
        UserEntity currentUser = userService.getCurrentUser();
        TeamEntity team = findActiveByUser(currentUser)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST))
            .getTeam();
        
        if(!teamAcceptsNewMembers(team))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        TeamJoinEntity invite = TeamJoinEntity.builder()
            .team(team)
            .user(user)
            .joinStatus(TeamJoinStatus.PENDING)
            .role(TeamRoleName.ROLE_TEAM_MEMBER)
            .build();

        return joinRepository.save(invite);

    }

    public TeamJoinEntity acceptInvite(UUID joinId) {
        UserEntity user = userService.getCurrentUser();
        findActiveByUser(user).ifPresent(join -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        });

        TeamJoinEntity join = findJoinById(joinId);

        if(!teamAcceptsNewMembers(join.getTeam()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            
        join.setResponseDate(LocalDateTime.now());

        return changeInviteStatus(join, TeamJoinStatus.ACTIVE);
    }

    public TeamJoinEntity refuseInvite(UUID joinId) {
        TeamJoinEntity join = findJoinById(joinId);
        join.setResponseDate(LocalDateTime.now());
        return changeInviteStatus(join, TeamJoinStatus.INVITE_REFUSED);
    }

    public TeamJoinEntity banUser(UserEntity userToBan) {
        UserEntity currentUser = userService.getCurrentUser();

        if(currentUser.getId().equals(userToBan.getId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        if(!sameTeam(currentUser, userToBan))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        
        TeamJoinEntity join = findActiveByUser(userToBan).get();
        join.setJoinStatus(TeamJoinStatus.BANNED);
        join.setActive(false);

        return joinRepository.save(join);
    }

    public TeamJoinEntity toCaptain(UserEntity newCaptain) {
        UserEntity currentUser = userService.getCurrentUser();

        if(currentUser.getId().equals(newCaptain.getId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        if(!sameTeam(currentUser, newCaptain))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        TeamJoinEntity joinCurrent = findActiveByUser(currentUser).get();
        joinCurrent.setRole(TeamRoleName.ROLE_TEAM_MEMBER);

        TeamJoinEntity joinNewCaptain = findActiveByUser(newCaptain).get();
        joinNewCaptain.setRole(TeamRoleName.ROLE_TEAM_CAPTAIN);

        return joinRepository.save(joinNewCaptain);
    }

    public TeamEntity findTeamById(UUID id) {
        return teamRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public TeamJoinEntity setOpenToPlay(boolean openToPlay) {
        TeamJoinEntity join = getCurrentTeam()
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        
        join.setOpenToPlay(openToPlay);
        return joinRepository.save(join);
    }

    public Collection<RiotAccEntity> findRiotAccByTeam(TeamEntity team) {
        Collection<TeamJoinEntity> members = findActiveByTeam(team);
        Collection<UserEntity> users = members.stream()
            .map(TeamJoinEntity::getUser)
            .toList();
        
        return riotService.findByUsers(users);
    }

    public TeamEntity findByRiotIds(List<String> userRiotIds) {
        List<TeamEntity> teams = riotService.findByRiotIds(userRiotIds).stream()
            .map(RiotAccEntity::getUser)
            .flatMap(user -> findActiveByUser(user).stream())
            .map(TeamJoinEntity::getTeam)
            .distinct()
            .toList();

        if (teams.size() == 1 && teams.get(0) != null) {
            return teams.get(0);
        }
        
        throw new NotFoundException("Team not found");
    }

    private TeamEntity saveTeam(TeamEntity team) {
        validateTeam(team);
        return teamRepository.save(team);
    }

    private boolean sameTeam(UserEntity user1, UserEntity user2) {
        
        TeamJoinEntity join1 = findActiveByUser(user1).orElse(null);
        TeamJoinEntity join2 = findActiveByUser(user2).orElse(null);
        if (join1 == null || join2 == null) {
            return false;
        }
        return join1.getTeam().equals(join2.getTeam());
    }
    
    private TeamJoinEntity changeInviteStatus(TeamJoinEntity invite, TeamJoinStatus status) {
        if (!invite.getJoinStatus().equals(TeamJoinStatus.PENDING))
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        
        UserEntity user = userService.getCurrentUser();
        if(!invite.getUser().getId().equals(user.getId()))
        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        
        invite.setJoinStatus(status);
        return joinRepository.save(invite);
    }
    
    private TeamJoinEntity findJoinById(UUID id) {
        return joinRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    
    private void randomizeNewCaptain(TeamEntity team) {
        Collection<TeamJoinEntity> data = joinRepository.findActiveByTeam(team);
        if (data.isEmpty()) return;
        
        List<TeamJoinEntity> joins = new ArrayList<>(data);
        Random random = new Random();
        
        int index = random.nextInt(joins.size());
        TeamJoinEntity randomJoin = joins.get(index);
        
        randomJoin.setRole(TeamRoleName.ROLE_TEAM_CAPTAIN);
        
        joinRepository.save(randomJoin);
    }
    
    private boolean teamAcceptsNewMembers(TeamEntity team) {
        return findActiveByTeam(team).size() < 5;
    }

    private void validateTeam(TeamEntity team) {
        List<String> errors = new ArrayList<>();
    
        for (ITeamValidation validation : validations) {
            if(!validation.validate(team))
                errors.add(validation.getMessage());
        }
    
        if (!errors.isEmpty())
            throw new BadRequestException(errors);
    }
    
}
