package br.com.jrr.apiTest.Match;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jrr.apiTest.App.Exceptions.BadRequestException;
import br.com.jrr.apiTest.App.Exceptions.NotFoundException;
import br.com.jrr.apiTest.Match.DTOs.FromRiotApi.MatchInfoDTO;
import br.com.jrr.apiTest.Match.DTOs.FromRiotApi.MatchParticipantDTO;
import br.com.jrr.apiTest.Match.DTOs.FromRiotApi.MatchRegisterDTO;
import br.com.jrr.apiTest.Match.Enums.MatchStatus;
import br.com.jrr.apiTest.PyRequest.PyRequestService;
import br.com.jrr.apiTest.Tournament.TournamentService;
import br.com.jrr.apiTest.Tournament.Entity.TournamentEntity;
import br.com.jrr.apiTest.Tournament.Entity.TournamentJoinEntity;
import br.com.jrr.apiTest.Tournament.Enum.TournamentJoinStatus;

@Service
public class MatchService {

    @Autowired
    private MatchRepository repository;

    @Autowired
    private TournamentService tournamentService;

    @Autowired
    private PyRequestService requestService;
    
    public MatchEntity registerMatch(UUID tournamentId, MatchRegisterDTO dto) {
        String riotId = dto.metadata().matchId();
        repository.findByRiotId(riotId)
            .ifPresent(m -> { throw new BadRequestException("Riot ID already registred"); });

        TournamentJoinEntity join1 = findParticipantByMatchInfo(dto.info(), 100);
        TournamentJoinEntity join2 = findParticipantByMatchInfo(dto.info(), 200);

        validateTournament(join1, join2, tournamentId);

        MatchStatus status = defineStatus(dto.info());
        
        MatchEntity entity = MatchEntity.builder()
        .riotId(riotId)
        .join1(join1)
        .join2(join2)
        .startDate(LocalDateTime.ofInstant(
            Instant.ofEpochMilli(dto.metadata().gameCreation()),
            ZoneOffset.UTC)
            )
            .status(status)
            .build();
            
        tournamentService.processRound(entity.getWinner(), entity.getLoser());
        
        entity = updateMetadata(entity);
        return repository.save(entity);
    }

    public Collection<MatchEntity> findAllByTournament(UUID tournamentId) {
        TournamentEntity tournament = tournamentService.findById(tournamentId);
        Collection<TournamentJoinEntity> joins = tournamentService.findAllByTournament(tournament);
        System.out.println("\n\n");
        System.out.println(joins.size());
        System.out.println("\n\n");
        return repository.findAllByJoins(joins);
    }

    public String getMetadataById(UUID id) {
        MatchEntity match = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Match not found"));
        return match.getMetaData();
    } 

    private TournamentJoinEntity findParticipantByMatchInfo(MatchInfoDTO matchInfo, int teamId) {
        List<String> puuids = exctractPuuids(matchInfo, teamId);
        TournamentJoinEntity join = tournamentService.findOpenByPuuids(puuids);
        if(!join.getStatus().equals(TournamentJoinStatus.PLAYING))
            throw new BadRequestException(String.format("Team %s are not playing", join.getTeam().getName()));

        return join;
    }

    private List<String> exctractPuuids(MatchInfoDTO info, int teamId) {
        return info.participants()
            .stream()
            .filter(p -> p.teamId() == teamId)
            .map(MatchParticipantDTO::puuid)
            .toList();
    }

    private void validateTournament(TournamentJoinEntity join1, TournamentJoinEntity join2, UUID tournamentId) {
        TournamentEntity tournament1 = join1.getTournament();
        TournamentEntity tournament2 = join2.getTournament();

        if (!(tournament1.getId().equals(tournamentId)) || !(tournament2.getId().equals(tournamentId)))
            throw new BadRequestException("These teams are not competing in the same tournament");
        
    }

    private MatchStatus defineStatus(MatchInfoDTO info) {

        if (!info.endOfGameResult().equals("GameComplete"))
            return MatchStatus.PENDING;
        
        int teamWinnerId = info.teams().get(0).win()
            ? info.teams().get(0).teamId()
            : info.teams().get(1).teamId();

        return teamWinnerId == 100 ? MatchStatus.TEAM_1_WINS : MatchStatus.TEAM_2_WINS;
    }

    protected MatchEntity updateMetadata(MatchEntity match) {
        String metadata = requestService.requestMatchMetaData(match.getRiotId());

        if(metadata != null)
            match.setMetaData(metadata);

        return match;
    }

}
