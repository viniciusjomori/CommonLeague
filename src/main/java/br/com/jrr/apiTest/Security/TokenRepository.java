package br.com.jrr.apiTest.Security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.jrr.apiTest.User.UserEntity;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<TokenEntity, UUID> {

    Optional<TokenEntity> findByToken(String token);

    @Query("SELECT t FROM TokenEntity t WHERE t.user = :user AND t.active = true")
    Collection<TokenEntity> findActiveByUser(@Param("user") UserEntity user);
}
