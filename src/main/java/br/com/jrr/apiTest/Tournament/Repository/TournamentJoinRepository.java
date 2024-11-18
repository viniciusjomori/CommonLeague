package br.com.jrr.apiTest.Tournament.Repository;

import java.util.Collection;
import java.util.Optional;

import org.hibernate.validator.constraints.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.jrr.apiTest.Team.Entity.TeamEntity;
import br.com.jrr.apiTest.Tournament.Entity.TournamentEntity;
import br.com.jrr.apiTest.Tournament.Entity.TournamentJoinEntity;

public interface TournamentJoinRepository extends JpaRepository<TournamentJoinEntity, UUID> {

    @Query("SELECT j FROM TournamentJoinEntity j WHERE j.team = :team AND exitDate IS NULL AND j.active = true")
    Optional<TournamentJoinEntity> findOpenByTeam(TeamEntity team);

    @Query("SELECT j FROM TournamentJoinEntity j WHERE j.tournament = :tournament AND j.active = true")
    Collection<TournamentJoinEntity> findAllByTournament(TournamentEntity tournament);

    @Query("SELECT j FROM TournamentJoinEntity j WHERE j.tournament = :tournament AND j.status = 'WAITING_TOURNAMENT' AND j.active = true")
    Collection<TournamentJoinEntity> findWaitingForTournament(TournamentEntity tournament);

    @Query("SELECT j FROM TournamentJoinEntity j WHERE j.tournament = :tournament AND j.status = 'PLAYING' AND j.active = true")
    Collection<TournamentJoinEntity> findPlayingByTournament(TournamentEntity tournament);

    @Query("SELECT j FROM TournamentJoinEntity j WHERE j.tournament = :tournament AND j.status = 'LOSE' AND j.active = true")
    Collection<TournamentJoinEntity> findLoseByTournament(TournamentEntity tournament);

    @Query("SELECT j FROM TournamentJoinEntity j WHERE j.team = :team AND j.status = 'WAITING_TOURNAMENT' AND j.active = true")
    Optional<TournamentJoinEntity> findWaitingForTournament(TeamEntity team);

    @Query("SELECT j FROM TournamentJoinEntity j WHERE j.team = :team")
    Collection<TournamentJoinEntity> findAllByTeam(TeamEntity team);

    @Query("SELECT j FROM TournamentJoinEntity j WHERE j.tournament = :tournament AND j.status = 'WAITING_ROUND' AND j.active = true")
    Collection<TournamentJoinEntity> findWaitingForRound(TournamentEntity tournament);

}
