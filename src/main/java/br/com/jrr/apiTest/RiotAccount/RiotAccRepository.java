package br.com.jrr.apiTest.RiotAccount;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.jrr.apiTest.User.UserEntity;

public interface RiotAccRepository extends JpaRepository<RiotAccEntity, UUID> {
    
    @Query("SELECT l FROM RiotAccEntity l WHERE l.user = :user AND l.active = true")
    Optional<RiotAccEntity> findActiveByUser(UserEntity user);

    @Query("SELECT l FROM RiotAccEntity l WHERE l.user IN :users AND l.active = true")
    Collection<RiotAccEntity> findActiveByUsers(Collection<UserEntity> users);

    @Query("SELECT l FROM RiotAccEntity l WHERE l.puuid IN :riotIds AND l.active = true")
    Collection<RiotAccEntity> findActiveByRiotIds(Collection<String> riotIds);

    @Query("SELECT l FROM RiotAccEntity l WHERE l.gameName = :gameName AND tagLine = :tagLine AND l.active = true")
    Optional<RiotAccEntity> findByPlayerInfo(String gameName, String tagLine);
}
