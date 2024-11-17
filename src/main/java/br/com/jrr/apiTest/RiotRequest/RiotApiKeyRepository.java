package br.com.jrr.apiTest.RiotRequest;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RiotApiKeyRepository extends JpaRepository<RiotApiKeyEntity, UUID> {
    
    @Query("SELECT r FROM RiotApiKeyEntity r WHERE active = true")
    Optional<RiotApiKeyEntity> findActive();

}
