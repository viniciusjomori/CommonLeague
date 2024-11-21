package br.com.jrr.apiTest.Tournament;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.com.jrr.apiTest.App.Exceptions.BadRequestException;
import br.com.jrr.apiTest.App.Exceptions.ConflictException;
import br.com.jrr.apiTest.App.Exceptions.ForbiddenException;
import br.com.jrr.apiTest.App.Exceptions.NotFoundException;
import br.com.jrr.apiTest.RiotAccount.RiotAccEntity;
import br.com.jrr.apiTest.RiotRequest.RiotRequestService;
import br.com.jrr.apiTest.Team.Entity.TeamEntity;
import br.com.jrr.apiTest.Team.Entity.TeamJoinEntity;
import br.com.jrr.apiTest.Team.Service.TeamService;
import br.com.jrr.apiTest.Tournament.DTOs.RiotAPI.TournamentCodeRegisterDTO;
import br.com.jrr.apiTest.Tournament.Entity.TournamentEntity;
import br.com.jrr.apiTest.Tournament.Entity.TournamentJoinEntity;
import br.com.jrr.apiTest.Tournament.Enum.TournamentJoinStatus;
import br.com.jrr.apiTest.Tournament.Enum.TournamentStatus;
import br.com.jrr.apiTest.Tournament.Repository.TournamentJoinRepository;
import br.com.jrr.apiTest.Tournament.Repository.TournamentRepository;
import br.com.jrr.apiTest.Tournament.Strategy.IMembersValidation;
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

    @Autowired
    private RiotRequestService requestService;

    @Autowired
    private List<IMembersValidation> validations;


    @Transactional(isolation = Isolation.SERIALIZABLE)
    public TournamentJoinEntity joinTournament(int qntChips) {
        if(qntChips <= 0)
            throw new BadRequestException("Qnt cannot be 0 or negative");

        TeamEntity team = teamService.getCurrentTeam()
            .orElseThrow(() -> new ForbiddenException("You are not on a team"))
            .getTeam();

        joinRepository.findOpenByTeam(team)
            .ifPresent(j -> { throw new BadRequestException("Your team is already in a tournament"); });
        
        Collection<TeamJoinEntity> members = teamService.findActiveByTeam(team);
        
        validateMembers(members);

        TournamentEntity tournament = findOpenByQntChips(qntChips);
        
        TournamentJoinEntity join = TournamentJoinEntity.builder()
            .team(team)
            .tournament(tournament)
            .status(TournamentJoinStatus.WAITING_TOURNAMENT)
            .round(1)
            .build();
        
        transactionService.createForTournament(members, qntChips, tournament);
        
        return joinRepository.save(join);
        
    }

    public boolean isOpenToPlay(Collection<TeamJoinEntity> members) {
        return members.size() == 5 && members
            .stream()
            .allMatch(TeamJoinEntity::isOpenToPlay);
    }

    public TournamentJoinEntity getCurrentOpenJoin() {
        TeamEntity team = teamService.getCurrentTeam()
            .orElseThrow(() -> new ForbiddenException("You're not on a team"))
            .getTeam();
        
        return findOpenByTeam(team)
            .orElseThrow(() -> new NotFoundException("No tournament"));
    }

    public Collection<TournamentJoinEntity> findJoinsByCurrentTeam(){
        Optional<TeamJoinEntity> team = teamService.getCurrentTeam();

        return team.isPresent() ? joinRepository.findAllByTeam(team.get().getTeam()) : new ArrayList<>();
    }
    
    public TournamentJoinEntity cancelJoin() {
        TeamEntity team = teamService.getCurrentTeam()
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST))
            .getTeam();
        TournamentJoinEntity tournamentJoin = joinRepository
            .findWaitingForTournament(team)
            .orElseThrow(() -> new BadRequestException("You are not waiting a tournament"));
        
        Collection<TeamJoinEntity> members = teamService.findActiveByTeam(team);
        TournamentEntity tournament = tournamentJoin.getTournament();

        transactionService.refundTournament(members, tournament);
        
        tournamentJoin.setStatus(TournamentJoinStatus.LEFT);
        tournamentJoin.setActive(false);
        tournamentJoin.setExitDate(LocalDateTime.now());
        
        return joinRepository.save(tournamentJoin);
    }

    public void startTournament(TournamentEntity tournament) {
        Collection<TournamentJoinEntity> joins = joinRepository.findWaitingForTournament(tournament);
        validateTournament(joins);
        
        int providerId = requestService.requestProviderId(tournament);
        String tournamentRiotId = requestService.requestTournament(tournament, providerId);
        requestTournamentCode(joins, providerId);
        
        tournament.setStatus(TournamentStatus.IN_PROGRESS);
        tournament.setRiotId(tournamentRiotId);
        tournamentRepository.save(tournament);
        
        for (TournamentJoinEntity join : joins) {
            join.setStatus(TournamentJoinStatus.PLAYING);
            joinRepository.save(join);
        }
        
    }

    public Optional<TournamentJoinEntity> findOpenByTeam(TeamEntity team) {
        return joinRepository.findOpenByTeam(team);
    }

    public TournamentEntity findById(UUID id) {
        return tournamentRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Tournament not found"));
    }

    public Collection<TournamentEntity> findAllByTeam(TeamEntity team) {
        Collection<TournamentJoinEntity> joins = joinRepository.findAllByTeam(team);
        return joins.stream()
            .map(TournamentJoinEntity::getTournament)
            .toList();
    }

    public Collection<TournamentJoinEntity> findAllByTournament(TournamentEntity tournament) {
        return joinRepository.findAllByTournament(tournament);
    }

    public Collection<TournamentEntity> findAllByCurrentTeam() {
        TeamEntity team = teamService.getCurrentTeam()
            .orElseThrow(() -> new NotFoundException("Team not found"))
            .getTeam();
        return findAllByTeam(team);
    }

    public TournamentJoinEntity findOpenByPuuids(List<String> puuids) {
        TeamEntity team = teamService.findByRiotIds(puuids);

        return findOpenByTeam(team)
            .orElseThrow(() -> new BadRequestException(
                String.format("Team %s is not in a tournament", team.getName())
            ));
    }

    public void processRound(TournamentJoinEntity winner, TournamentJoinEntity loser) {
        loseTournament(loser);
        
        Collection<TournamentJoinEntity> joins = joinRepository.findAllByTournament(winner.getTournament());
        
        if(nextRound(joins)) return;
        if(finishTournament(joins)) return;
        
        winner.setStatus(TournamentJoinStatus.WAITING_ROUND);
        joinRepository.save(winner);
    }

    private void loseTournament(TournamentJoinEntity loser) {
        loser.setStatus(TournamentJoinStatus.LOSE);
        loser.setExitDate(LocalDateTime.now());
        joinRepository.save(loser);
    }

    private boolean nextRound(Collection<TournamentJoinEntity> joins) {
        boolean canNextRound =
            joins.stream().filter(j -> j.getStatus().equals(TournamentJoinStatus.WAITING_ROUND)).count() > 0
            && joins.stream().filter(j -> j.getStatus().equals(TournamentJoinStatus.PLAYING)).count() == 1;

        if(!canNextRound)
            return false;

        joins.stream()
            .filter(j -> !j.getStatus().equals(TournamentJoinStatus.LOSE))
            .forEach(join -> {
                join.setStatus(TournamentJoinStatus.PLAYING);
                join.setRound(join.getRound() + 1);
                joinRepository.save(join);
            });
        
        return true;
    }

    private boolean finishTournament(Collection<TournamentJoinEntity> joins) {
        boolean canFinishTournament = 
            joins.stream().filter(j -> j.getStatus().equals(TournamentJoinStatus.PLAYING)).count() == 1
            && joins.stream().filter(j -> j.getStatus().equals(TournamentJoinStatus.LOSE)).count() == joins.size()-1;

        if (!canFinishTournament)
            return false;

        TournamentJoinEntity winnerTournamentJoin = joins.stream()
            .filter(j -> j.getStatus().equals(TournamentJoinStatus.PLAYING))
            .findFirst()
            .get();

        TournamentEntity tournament = winnerTournamentJoin.getTournament();

        winnerTournamentJoin.setStatus(TournamentJoinStatus.WIN);
        winnerTournamentJoin.setExitDate(LocalDateTime.now());
        joinRepository.save(winnerTournamentJoin);

        Collection<TeamJoinEntity> winners = teamService.findActiveByTeam(winnerTournamentJoin.getTeam());

        Collection<TournamentJoinEntity> losersTournamentJoin = joinRepository.findLoseByTournament(tournament);
        Collection<TeamJoinEntity> losers = losersTournamentJoin.stream()
            .map(TournamentJoinEntity::getTeam)
            .map(teamService::findActiveByTeam)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
        
        tournament.setStatus(TournamentStatus.FINISHED);
        tournament.setEndDate(LocalDateTime.now());
        tournamentRepository.save(tournament);

        transactionService.rewardTournament(tournament, winners, losers);
        
        return true;
    }

    private void validateMembers(Collection<TeamJoinEntity> members) {
        List<String> errors = new ArrayList<>();

        for (IMembersValidation validation : validations) {
            if(!validation.validate(members))
                errors.add(validation.getMessage());
        }

        if (!errors.isEmpty())
            throw new BadRequestException(errors);
    }

    private TournamentEntity findOpenByQntChips(int qntChipPerPlayer) {
        return tournamentRepository.findOpenByQnt(qntChipPerPlayer)
            .orElseGet(() -> registerNewTournament(qntChipPerPlayer));
    }
    
    private void validateTournament(Collection<TournamentJoinEntity> joins) {
        int size = joins.size();
        double log2 = Math.log(size) / Math.log(2);
        if(!(log2 == (int) log2) && size < 4)
            throw new ConflictException("Tournament cannot start");
    }

    private TournamentEntity registerNewTournament(int qntChipPerPlayer) {
        TournamentEntity tournament = TournamentEntity.builder()
            .qntChipsPerPlayer(qntChipPerPlayer)
            .status(TournamentStatus.PENDING)
            .build();
        return tournamentRepository.save(tournament);
    }

    private String requestTournamentCode(Collection<TournamentJoinEntity> joins, int riotId) {
        Collection<String> puuids = joins.stream()
            .map(join -> teamService.findRiotAccByTeam(join.getTeam()))
            .flatMap(Collection::stream)
            .map(RiotAccEntity::getPuuid)
            .toList();

        String metadata = "";
        int teamSize = puuids.size() / joins.size();
        String pickType = "BLIND_PICK";
        String mapType = "SUMMONERS_RIFT";
        String spectatorType = "ALL";
        boolean enoughPlayers = true;

        TournamentCodeRegisterDTO request = new TournamentCodeRegisterDTO(
            puuids,
            metadata,
            teamSize,
            pickType,
            enoughPlayers,
            spectatorType,
            mapType
        );

        int numberOfMatchs = joins.size() -1;
        return requestService.requestTournamentCode(request, numberOfMatchs, riotId);

    }

}
