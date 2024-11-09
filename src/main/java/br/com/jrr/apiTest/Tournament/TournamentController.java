package br.com.jrr.apiTest.Tournament;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.jrr.apiTest.App.ResponseDTO;
import br.com.jrr.apiTest.Tournament.Entity.TournamentJoinEntity;
import br.com.jrr.apiTest.Tournament.Mapper.TournamentJoinMapper;
import jakarta.annotation.security.RolesAllowed;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("tournament")
public class TournamentController {

    @Autowired
    private TournamentService service;

    @Autowired
    private TournamentJoinMapper joinMapper;
    
    @PostMapping("join")
    @RolesAllowed("TEAM_CAPTAIN")
    public ResponseEntity<TournamentJoinResponseDTO> joinTournament(@RequestParam("qntChips") int qntChips) {
        TournamentJoinEntity join = service.joinTournament(qntChips);
        TournamentJoinResponseDTO response = joinMapper.toResponse(join);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @RolesAllowed({"TEAM_MEMBER", "TEAM_CAPTAIN"})
    public ResponseEntity<TournamentJoinResponseDTO> getCurrentTournament() {
        TournamentJoinEntity join = service.getCurrentJoin();
        TournamentJoinResponseDTO response = joinMapper.toResponse(join);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    @RolesAllowed({"TEAM_CAPTAIN"})
    public ResponseEntity<ResponseDTO> cancelTournamentJoin() {
        service.cancelJoin();
        ResponseDTO response = new ResponseDTO(HttpStatus.OK, "Join Tournament Cancelled");
        return ResponseEntity.ok(response);
    }

}