package br.com.jrr.apiTest.Match;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.jrr.apiTest.Tournament.Entity.TournamentJoinEntity;

public interface MatchRepository extends JpaRepository<MatchEntity, UUID> {
    
    Optional<MatchEntity> findByRiotId(String riotId);

    @Query("SELECT m FROM MatchEntity m WHERE join1 IN :joins OR join2 IN :joins")
    Collection<MatchEntity> findAllByJoins(Collection<TournamentJoinEntity> joins);

}
