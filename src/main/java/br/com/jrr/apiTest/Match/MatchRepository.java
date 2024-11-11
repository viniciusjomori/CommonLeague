package br.com.jrr.apiTest.Match;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<MatchEntity, UUID> {
    
    Optional<MatchEntity> findByRiotId(String riotId);

}
