package br.com.jrr.apiTest.Tournament;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.com.jrr.apiTest.App.Exceptions.BadRequestException;
import br.com.jrr.apiTest.App.Exceptions.ConflictException;
import br.com.jrr.apiTest.App.Exceptions.ForbiddenException;
import br.com.jrr.apiTest.App.Exceptions.InternalServerErrorException;
import br.com.jrr.apiTest.App.Exceptions.NotFoundException;
import br.com.jrr.apiTest.Request.HttpDTO;
import br.com.jrr.apiTest.Request.Enum.RequestMethod;
import br.com.jrr.apiTest.Request.Service.RequestService;
import br.com.jrr.apiTest.RiotAccount.RiotAccEntity;
import br.com.jrr.apiTest.Team.Entity.TeamEntity;
import br.com.jrr.apiTest.Team.Entity.TeamJoinEntity;
import br.com.jrr.apiTest.Team.Service.TeamService;
import br.com.jrr.apiTest.Tournament.DTOs.RiotAPI.ProviderRequestDTO;
import br.com.jrr.apiTest.Tournament.DTOs.RiotAPI.TournamentCodeRegisterDTO;
import br.com.jrr.apiTest.Tournament.DTOs.RiotAPI.TournamentConnectDTO;
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
    private RequestService requestService;

    @Autowired
    private List<IMembersValidation> validations;

    @Value("${lol.api-key}")
    private String apiKey;

    @Value("${lol.base-dns}")
    private String baseDns;

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
            .status(TournamentJoinStatus.WAITING)
            .build();
        
        transactionService.createForTournament(members, qntChips, tournament);
        
        return joinRepository.save(join);
        
    }

    public boolean isOpenToPlay(Collection<TeamJoinEntity> members) {
        return members.size() == 5 && members
            .stream()
            .allMatch(TeamJoinEntity::isOpenToPlay);
    }

    public TournamentJoinEntity getCurrentJoin() {
        TeamEntity team = teamService.getCurrentTeam()
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN))
            .getTeam();
        
        return findOpenByTeam(team)
            .orElseThrow(() -> new NotFoundException("No tournament"));
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
        tournamentJoin.setExitDate(LocalDateTime.now());
        
        return joinRepository.save(tournamentJoin);
    }

    public void startTournament(TournamentEntity tournament) {
        Collection<TournamentJoinEntity> joins = joinRepository.findWaitingByTournament(tournament);
        validateTournament(joins);
        
        int providerId = requestProviderId(tournament);
        String tournamentRiotId = requestTournament(tournament, providerId);
        requestTournamentCode(joins, providerId);
        
        tournament.setStatus(TournamentStatus.IN_PROGRESS);
        tournament.setRiotId(tournamentRiotId);
        tournamentRepository.save(tournament);
        
        for (TournamentJoinEntity join : joins) {
            join.setStatus(TournamentJoinStatus.PLAYING);
            joinRepository.save(join);
        }
        
    }

    public void finishTournament(TournamentEntity tournament) {
        Collection<TournamentJoinEntity> joins = joinRepository.findPlayingByTournament(tournament);

        if (joins.size() > 1)
            return;

        TournamentJoinEntity winnerTournamentJoin = joins.iterator().next();

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
    }

    public Optional<TournamentJoinEntity> findOpenByTeam(TeamEntity team) {
        return joinRepository.findOpenByTeam(team);
    }

    public Collection<TournamentJoinEntity> findOpenByTournament(TournamentEntity tournament) {
        return joinRepository.findOpenByTournament(tournament);
    }

    public TournamentEntity findById(UUID id) {
        return tournamentRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Tournament not found"));
    }

    public Collection<TournamentEntity> findAllByTeam(TeamEntity team) {
        Collection<TournamentJoinEntity> joins = joinRepository.findActiveByTeam(team);
        return joins.stream()
            .map(TournamentJoinEntity::getTournament)
            .toList();
    }

    public Collection<TournamentEntity> findAllByCurrentTeam() {
        TeamEntity team = teamService.getCurrentTeam()
            .orElseThrow(() -> new NotFoundException("Team not found"))
            .getTeam();
        return findAllByTeam(team);
    }

    public TournamentJoinEntity findActiveByPuuids(List<String> puuids) {
        TeamEntity team = teamService.findByRiotIds(puuids);

        return findOpenByTeam(team)
            .orElseThrow(() -> new BadRequestException(
                String.format("Team %s is not in a tournament", team.getName())
            ));
    }

    public TournamentJoinEntity loseTournament(TournamentJoinEntity join) {
        join.setStatus(TournamentJoinStatus.LOSE);
        join.setExitDate(LocalDateTime.now());
        return joinRepository.save(join);
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
        if(!(log2 == (int) log2))
            throw new ConflictException("Tournament cannot start");
    }

    private TournamentEntity registerNewTournament(int qntChipPerPlayer) {
        TournamentEntity tournament = TournamentEntity.builder()
            .qntChipsPerPlayer(qntChipPerPlayer)
            .status(TournamentStatus.PENDING)
            .build();
        return tournamentRepository.save(tournament);
    }
    
    private int requestProviderId(TournamentEntity tournament) {
        ProviderRequestDTO request = new ProviderRequestDTO(
            "BR", 
            "http://localhost:8080/match?tournament=" + tournament.getId().toString()
        );

        String endpoint = baseDns + "lol/tournament-stub/v5/providers?api_key=" + apiKey;
        HttpDTO response = requestService.request(endpoint, RequestMethod.POST, request);

        if(response.statusCode() == 200)
            return Integer.parseInt(response.jsonBody());
        
        throw new InternalServerErrorException();
    }

    private String requestTournament(TournamentEntity entity, int providerId) {
        TournamentConnectDTO request = new TournamentConnectDTO(entity.getId().toString(), providerId);
        
        String endpoint = baseDns + "lol/tournament-stub/v5/tournaments?api_key=" + apiKey;
        HttpDTO response = requestService.request(endpoint, RequestMethod.POST, request);

        if(response.statusCode() == 200)
            return response.jsonBody();
        
        throw new InternalServerErrorException();
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
        String endpoint = baseDns + "lol/tournament-stub/v5/codes?count=" + numberOfMatchs + "&tournamentId=" + riotId + "&api_key=" + apiKey;

        HttpDTO response = requestService.request(endpoint, RequestMethod.POST, request);

        if(response.statusCode() == 200) {
            return response.jsonBody();
        }

        throw new InternalServerErrorException();

    }

}
