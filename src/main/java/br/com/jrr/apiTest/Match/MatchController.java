package br.com.jrr.apiTest.Match;

import java.util.Collection;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.jrr.apiTest.Match.DTOs.MatchResponseDTO;
import br.com.jrr.apiTest.Match.DTOs.FromRiotApi.MatchRegisterDTO;
import jakarta.annotation.security.PermitAll;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("match")
public class MatchController {
    
    @Autowired
    private MatchService service;

    @Autowired
    private MatchMapper mapper;

    @PostMapping
    @PermitAll
    public ResponseEntity<MatchResponseDTO> registerForTournament(
        @RequestParam("tournament") UUID tournamentId,
        @RequestBody MatchRegisterDTO dto) {

            MatchEntity entity = service.registerMatch(tournamentId, dto);
            return ResponseEntity.ok(mapper.toResponse(entity));
    }

    @PermitAll
    @GetMapping
    public ResponseEntity<Collection<MatchResponseDTO>> findByTournament(@RequestParam("tournament") UUID tournamentId) {
        Collection<MatchEntity> matchs = service.findAllByTournament(tournamentId);
        return ResponseEntity.ok(mapper.toResponse(matchs));
    }

    @GetMapping("{id}")
    @PermitAll
    public ResponseEntity<MatchResponseDTO> findById(@PathVariable UUID id) {
        MatchEntity match = service.findById(id);
        return ResponseEntity.ok(mapper.toResponse(match));
    }

    @PermitAll
    @GetMapping("{id}/metadata")
    public ResponseEntity<String> findMetadata(@PathVariable UUID id) {
        MatchEntity match = service.findById(id);
        String metadata = match.getMetaData();
        return ResponseEntity.ok(metadata);
    }

}
