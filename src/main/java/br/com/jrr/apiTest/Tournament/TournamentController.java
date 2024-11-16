package br.com.jrr.apiTest.Tournament;

import java.util.Collection;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.jrr.apiTest.App.ResponseDTO;
import br.com.jrr.apiTest.Tournament.DTOs.TournamentJoinResponseDTO;
import br.com.jrr.apiTest.Tournament.DTOs.TournamentResponseDTO;
import br.com.jrr.apiTest.Tournament.Entity.TournamentEntity;
import br.com.jrr.apiTest.Tournament.Entity.TournamentJoinEntity;
import br.com.jrr.apiTest.Tournament.Mapper.TournamentJoinMapper;
import br.com.jrr.apiTest.Tournament.Mapper.TournamentMapper;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("tournament")
public class TournamentController {

    @Autowired
    private TournamentService service;

    @Autowired
    private TournamentMapper tournamentMapper;

    @Autowired
    private TournamentJoinMapper joinMapper;
    
    @PostMapping("join")
    @RolesAllowed("TEAM_CAPTAIN")
    public ResponseEntity<TournamentJoinResponseDTO> joinTournament(@RequestParam("qntChips") int qntChips) {
        TournamentJoinEntity join = service.joinTournament(qntChips);
        TournamentJoinResponseDTO response = joinMapper.toResponse(join);
        return ResponseEntity.ok(response);
    }

    @GetMapping("join")
    @RolesAllowed({"TEAM_MEMBER", "TEAM_CAPTAIN"})
    public ResponseEntity<TournamentJoinResponseDTO> getCurrentTournamentJoin() {
        TournamentJoinEntity join = service.getCurrentOpenJoin();
        TournamentJoinResponseDTO response = joinMapper.toResponse(join);
        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    @PermitAll
    public ResponseEntity<TournamentResponseDTO> findById(@PathVariable UUID id) {
        TournamentEntity entity = service.findById(id);
        TournamentResponseDTO response = tournamentMapper.toResponse(entity);
        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}/teams")
    @PermitAll
    public ResponseEntity<Collection<TournamentJoinResponseDTO>> findTeamsByTournament(@PathVariable UUID id) {
        TournamentEntity tournament = service.findById(id);
        Collection<TournamentJoinEntity> joins = service.findAllByTournament(tournament);
        Collection<TournamentJoinResponseDTO> response = joinMapper.toResponse(joins);
        return ResponseEntity.ok(response);
    }

    @GetMapping()
    @RolesAllowed({"TEAM_MEMBER", "TEAM_CAPTAIN"})
    public ResponseEntity<Collection<TournamentResponseDTO>> findAll() {
        Collection<TournamentEntity> tournaments = service.findAllByCurrentTeam();
        return ResponseEntity.ok(tournamentMapper.toResponse(tournaments));
    }

    @DeleteMapping
    @RolesAllowed({"TEAM_CAPTAIN"})
    public ResponseEntity<ResponseDTO> cancelTournamentJoin() {
        service.cancelJoin();
        ResponseDTO response = new ResponseDTO(HttpStatus.OK, "Join Tournament Cancelled");
        return ResponseEntity.ok(response);
    }

    @PostMapping("{id}/start")
    @RolesAllowed("ADMIN")
    public ResponseEntity<ResponseDTO> startTournament(@PathVariable UUID id) {
        TournamentEntity tournament = service.findById(id);
        service.startTournament(tournament);
        ResponseDTO response = new ResponseDTO(HttpStatus.OK, "Tournament started");
        return ResponseEntity.ok(response);
    }

}