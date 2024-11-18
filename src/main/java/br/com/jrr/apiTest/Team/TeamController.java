package br.com.jrr.apiTest.Team;

import java.util.Collection;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.jrr.apiTest.Team.DTO.InviteResponseDTO;
import br.com.jrr.apiTest.Team.DTO.MemberResponseDTO;
import br.com.jrr.apiTest.Team.DTO.TeamProfileDTO;
import br.com.jrr.apiTest.Team.DTO.TeamRegisterDTO;
import br.com.jrr.apiTest.Team.DTO.TeamResponseDTO;
import br.com.jrr.apiTest.Team.Entity.TeamEntity;
import br.com.jrr.apiTest.Team.Entity.TeamJoinEntity;
import br.com.jrr.apiTest.Team.Enum.TeamProfile;
import br.com.jrr.apiTest.Team.Mapper.TeamJoinMapper;
import br.com.jrr.apiTest.Team.Mapper.TeamMapper;
import br.com.jrr.apiTest.Team.Service.TeamService;
import br.com.jrr.apiTest.User.UserEntity;
import br.com.jrr.apiTest.User.UserService;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("team")
public class TeamController {
    
    @Autowired
    private TeamService teamService;

    @Autowired
    private UserService userService;

    @Autowired
    private TeamMapper teamMapper;

    @Autowired
    private TeamJoinMapper joinMapper;

    @PostMapping
    @RolesAllowed("CLIENT")
    public ResponseEntity<TeamResponseDTO> registerTeam(@RequestBody @Valid TeamRegisterDTO dto) {
        TeamEntity entity = teamService.registerTeam(dto);
        return ResponseEntity.ok(teamMapper.toResponse(entity));
    }

    @GetMapping("/current")
    @RolesAllowed({"TEAM_MEMBER", "TEAM_CAPTAIN"})
    public ResponseEntity<TeamResponseDTO> findCurrentTeam() {
        TeamEntity team = teamService
            .getCurrentTeam()
            .get()
            .getTeam();
        
        return ResponseEntity.ok(teamMapper.toResponse(team));
    }

    @PutMapping("/current")
    @RolesAllowed({"TEAM_MEMBER", "TEAM_CAPTAIN"})
    public ResponseEntity<MemberResponseDTO> setOpenToPlay(@PathParam("openToPlay") boolean openToPlay) {
        TeamJoinEntity entity = teamService.setOpenToPlay(openToPlay);
        return ResponseEntity.ok(joinMapper.toMember(entity));
    }

    @GetMapping
    @PermitAll
    public ResponseEntity<TeamResponseDTO> findFromUser(@RequestParam("user") UUID userId) {
        UserEntity user = userService.findById(userId);
        TeamEntity team = teamService
            .findActiveByUser(user)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))
            .getTeam();
        
        return ResponseEntity.ok(teamMapper.toResponse(team));
    }

    @GetMapping("/{id}/members")
    @PermitAll
    public ResponseEntity<Collection<MemberResponseDTO>> findTeamMembers(@PathVariable UUID id) {
        TeamEntity team = teamService.findTeamById(id);
        Collection<TeamJoinEntity> joins = teamService.findActiveByTeam(team);
        return ResponseEntity.ok(joinMapper.toMember(joins));
    }

    @DeleteMapping("left")
    @RolesAllowed({"TEAM_MEMBER", "TEAM_CAPTAIN"})
    @PreAuthorize("hasAuthority('UNLOCKED')")
    public ResponseEntity<MemberResponseDTO> leftTeam() {
        TeamJoinEntity join = teamService.leftTeam();
        return ResponseEntity.ok(joinMapper.toMember(join));
    }

    @PostMapping("/invite")
    @RolesAllowed({"TEAM_MEMBER", "TEAM_CAPTAIN"})
    @PreAuthorize("hasAuthority('UNLOCKED')")
    public ResponseEntity<InviteResponseDTO> inviteUser(@RequestParam("user") UUID userId) {
        UserEntity user = userService.findById(userId);
        TeamJoinEntity invite = teamService.inviteUser(user);
        return ResponseEntity.ok(joinMapper.toInvite(invite));
    }

    @PutMapping("/invite/{id}/accept")
    @RolesAllowed("CLIENT")
    public ResponseEntity<InviteResponseDTO> acceptInvite(@PathVariable UUID id) {
        TeamJoinEntity invite = teamService.acceptInvite(id);
        return ResponseEntity.ok(joinMapper.toInvite(invite));
    }

    @PutMapping("/invite/{id}/refuse")
    @RolesAllowed("CLIENT")
    public ResponseEntity<InviteResponseDTO> refuseInvite(@PathVariable UUID id) {
        TeamJoinEntity invite = teamService.refuseInvite(id);
        return ResponseEntity.ok(joinMapper.toInvite(invite));
    }

    @DeleteMapping("/user/{id}")
    @RolesAllowed("TEAM_CAPTAIN")
    @PreAuthorize("hasAuthority('UNLOCKED')")
    public ResponseEntity<MemberResponseDTO> banUser(@PathVariable UUID id) {
        UserEntity user = userService.findById(id);
        TeamJoinEntity join = teamService.banUser(user);
        return ResponseEntity.ok(joinMapper.toMember(join));
    }

    @PutMapping("/user/{id}/to-captain")
    @RolesAllowed("TEAM_CAPTAIN")
    @PreAuthorize("hasAuthority('UNLOCKED')")
    public ResponseEntity<MemberResponseDTO> toCaptain(@PathVariable UUID id) {
        UserEntity user = userService.findById(id);
        TeamJoinEntity join = teamService.toCaptain(user);
        return ResponseEntity.ok(joinMapper.toMember(join));
    }

    @GetMapping("/profile")
    @PermitAll
    public ResponseEntity<Collection<TeamProfileDTO>> getProfiles() {
        return ResponseEntity.ok(TeamProfile.toDTOS());
    }


}
