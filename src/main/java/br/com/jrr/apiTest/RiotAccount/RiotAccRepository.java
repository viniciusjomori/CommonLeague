package br.com.jrr.apiTest.RiotAccount;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.jrr.apiTest.User.UserEntity;

public interface RiotAccRepository extends JpaRepository<RiotAccEntity, UUID> {
    
    @Query("SELECT l FROM RiotAccEntity l WHERE l.user = :user AND l.active = true")
    Optional<RiotAccEntity> findActiveByUser(UserEntity user);
}
