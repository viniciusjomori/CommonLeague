package br.com.jrr.apiTest.Tournament.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.jrr.apiTest.Tournament.Entity.TournamentEntity;
import br.com.jrr.apiTest.Tournament.Enum.TournamentStatus;

public interface TournamentRepository extends JpaRepository<TournamentEntity, UUID> {
    
    @Query("SELECT t FROM TournamentEntity t WHERE t.qntChipsPerPlayer = :qntChipsPerPlayer AND t.status = 'PENDING' AND t.active = true")
    Optional<TournamentEntity> findOpenByQnt(int qntChipsPerPlayer);

    Collection<TournamentEntity> findAllByStatus(TournamentStatus status);
}
