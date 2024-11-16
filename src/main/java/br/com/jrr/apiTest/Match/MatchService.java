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
import br.com.jrr.apiTest.Match.DTOs.FromRiotApi.MatchInfoDTO;
import br.com.jrr.apiTest.Match.DTOs.FromRiotApi.MatchParticipantDTO;
import br.com.jrr.apiTest.Match.DTOs.FromRiotApi.MatchRegisterDTO;
import br.com.jrr.apiTest.Match.Enums.MatchStatus;
import br.com.jrr.apiTest.Tournament.TournamentService;
import br.com.jrr.apiTest.Tournament.Entity.TournamentEntity;
import br.com.jrr.apiTest.Tournament.Entity.TournamentJoinEntity;

@Service
public class MatchService {

    @Autowired
    private MatchRepository repository;

    @Autowired
    private TournamentService tournamentService;
    
    public MatchEntity registerMatch(UUID tournamentId, MatchRegisterDTO dto) {
        String riotId = dto.metadata().matchId();
        repository.findByRiotId(riotId)
            .ifPresent(m -> { throw new BadRequestException("Riot ID already registred"); });

        List<String> puuidsFromTeam1 = exctractPuuids(dto.info(), 100);
        List<String> puuidsFromTeam2 = exctractPuuids(dto.info(), 200);

        TournamentJoinEntity join1 = tournamentService.findActiveByPuuids(puuidsFromTeam1);
        TournamentJoinEntity join2 = tournamentService.findActiveByPuuids(puuidsFromTeam2);

        TournamentEntity tournament = getTournament(join1, join2, tournamentId);

        MatchStatus status = defineStatus(dto.info());
        
        TournamentJoinEntity loser = status.equals(MatchStatus.TEAM_1_WINS) ? join2 : join1;
        tournamentService.loseTournament(loser);

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

        tournamentService.finishTournament(tournament);
        
        return repository.save(entity);
    }

    public Collection<MatchEntity> findAllByTournament(UUID tournamentId) {
        TournamentEntity tournament = tournamentService.findById(tournamentId);
        Collection<TournamentJoinEntity> joins = tournamentService.findOpenByTournament(tournament);
        System.out.println("\n\n");
        System.out.println(joins.size());
        System.out.println("\n\n");
        return repository.findAllByJoins(joins);
    }

    private List<String> exctractPuuids(MatchInfoDTO info, int teamId) {
        return info.participants()
            .stream()
            .filter(p -> p.teamId() == teamId)
            .map(MatchParticipantDTO::puuid)
            .toList();
    }

    private TournamentEntity getTournament(TournamentJoinEntity join1, TournamentJoinEntity join2, UUID tournamentId) {
        TournamentEntity tournament1 = join1.getTournament();
        TournamentEntity tournament2 = join2.getTournament();

        if (!(tournament1.getId().equals(tournamentId)) || !(tournament2.getId().equals(tournamentId)))
            throw new BadRequestException("These teams are not competing in the same tournament");
        
        return tournament1;
    }

    private MatchStatus defineStatus(MatchInfoDTO info) {

        if (!info.endOfGameResult().equals("GameComplete"))
            return MatchStatus.PENDING;
        
        int teamWinnerId = info.teams().get(0).win()
            ? info.teams().get(0).teamId()
            : info.teams().get(1).teamId();

        return teamWinnerId == 100 ? MatchStatus.TEAM_1_WINS : MatchStatus.TEAM_2_WINS;
    }

}
