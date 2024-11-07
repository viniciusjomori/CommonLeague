package br.com.jrr.apiTest.Tournament;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.com.jrr.apiTest.Team.Entity.TeamEntity;
import br.com.jrr.apiTest.Team.Entity.TeamJoinEntity;
import br.com.jrr.apiTest.Team.Service.TeamService;
import br.com.jrr.apiTest.Tournament.Entity.TournamentEntity;
import br.com.jrr.apiTest.Tournament.Entity.TournamentJoinEntity;
import br.com.jrr.apiTest.Tournament.Enum.TournamentJoinStatus;
import br.com.jrr.apiTest.Tournament.Enum.TournamentStatus;
import br.com.jrr.apiTest.Tournament.Repository.TournamentJoinRepository;
import br.com.jrr.apiTest.Tournament.Repository.TournamentRepository;
import br.com.jrr.apiTest.Transaction.TransactionService;

@Service
public class TournamentService {
    
    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private TournamentJoinRepository joinRepository;

    @Autowired
    private TeamService teamService;

    @Autowired
    private TransactionService transactionService;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public TournamentJoinEntity joinTournament(int qntChips) {
        if(qntChips <= 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        TeamEntity team = teamService.getCurrentTeam()
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN))
            .getTeam();

        joinRepository.findOpenByTeam(team)
            .ifPresent(j -> { throw new ResponseStatusException(HttpStatus.BAD_REQUEST); });
        
        Collection<TeamJoinEntity> members = teamService.findActiveByTeam(team);
        
        if (!isOpenToPlay(members))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        transactionService.createForTournament(members, qntChips);

        return joinTournament(qntChips, team);
        
    }

    public boolean isOpenToPlay(Collection<TeamJoinEntity> members) {
        return members.size() == 5 && members
            .stream()
            .allMatch(TeamJoinEntity::isOpenToPlay);
    }

    private TournamentJoinEntity joinTournament(int qntChip, TeamEntity team) {

        TournamentEntity tournament = findOpenByQntChips(qntChip);
        
        TournamentJoinEntity join = TournamentJoinEntity.builder()
            .team(team)
            .tournament(tournament)
            .status(TournamentJoinStatus.WAITING)
            .build();
        
        return joinRepository.save(join);
    }

    public TournamentJoinEntity getCurrentJoin() {
        TeamEntity team = teamService.getCurrentTeam()
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN))
            .getTeam();
        
        return joinRepository.findOpenByTeam(team)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public TournamentJoinEntity getCurrentActiveJoin(){
        TeamEntity team = teamService.getCurrentTeam()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN))
                .getTeam();

        return joinRepository.findActiveByTeam(team)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    
    public TournamentJoinEntity cancelJoin() {
        TournamentJoinEntity tournamentJoin = getCurrentJoin();
        TeamEntity team = teamService.getCurrentTeam()
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST))
            .getTeam();
        
        Collection<TeamJoinEntity> members = teamService.findActiveByTeam(team);
        TournamentEntity tournament = tournamentJoin.getTournament();

        transactionService.refundTournament(members, tournament);
        
        tournamentJoin.setStatus(TournamentJoinStatus.LEFT);
        tournamentJoin.setActive(false);
        
        return joinRepository.save(tournamentJoin);
    }

    protected void startTournaments() {
        Collection<TournamentEntity> tournaments = tournamentRepository
            .findAllByStatus(TournamentStatus.PENDING);

        for (TournamentEntity tournament : tournaments) {
            Collection<TournamentJoinEntity> joins = joinRepository
                .findActiveByTournament(tournament);

            if(joins.size() == 4) {
                tournament.setStatus(TournamentStatus.IN_PROGRESS);
                tournamentRepository.save(tournament);
            
                for (TournamentJoinEntity join : joins) {
                    join.setStatus(TournamentJoinStatus.PLAYING);
                    joinRepository.save(join);
                }
            }
        }
    }

    private TournamentEntity findOpenByQntChips(int qntChipPerPlayer) {
        return tournamentRepository.findOpenByQnt(qntChipPerPlayer)
            .orElseGet(() -> registerNewTournament(qntChipPerPlayer));
    }
    
    private TournamentEntity registerNewTournament(int qntChipPerPlayer) {
        TournamentEntity tournament = TournamentEntity.builder()
            .qntChipsPerPlayer(qntChipPerPlayer)
            .status(TournamentStatus.PENDING)
            .build();
        return tournamentRepository.save(tournament);
    }
    

}
