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
    
    @Query("SELECT j FROM TournamentJoinEntity j WHERE j.team = :team AND (j.status = 'WAITING' OR j.status = 'PLAYING') AND j.active = true")
    Optional<TournamentJoinEntity> findOpenByTeam(TeamEntity team);

    @Query("SELECT j FROM TournamentJoinEntity j WHERE j.team = :team AND j.status = 'WAITING' AND j.active = true")
    Optional<TournamentJoinEntity> findActiveByTeam(TeamEntity team);

    @Query("SELECT j FROM TournamentJoinEntity j WHERE j.tournament = :tournament AND j.status = 'WAITING' AND j.active = true")
    Collection<TournamentJoinEntity> findActiveByTournament(TournamentEntity tournament);

}
