package br.com.jrr.apiTest.Team.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.jrr.apiTest.Team.Entity.TeamEntity;
import br.com.jrr.apiTest.Team.Entity.TeamJoinEntity;
import br.com.jrr.apiTest.User.UserEntity;

public interface TeamJoinRepository extends JpaRepository<TeamJoinEntity, UUID> {
    
    @Query("SELECT j FROM TeamJoinEntity j WHERE j.user = :user AND joinStatus = 'ACTIVE' AND j.active = true")
    Optional<TeamJoinEntity> findActiveByUser(UserEntity user);

    @Query("SELECT j FROM TeamJoinEntity j WHERE j.user = :user AND j.joinStatus = 'PENDING' AND j.active = true")
    Collection<TeamJoinEntity> findPendingByUser(UserEntity user);

    @Query("SELECT j FROM TeamJoinEntity j WHERE j.team = :team AND joinStatus = 'ACTIVE' AND j.active = true")
    Collection<TeamJoinEntity> findActiveByTeam(TeamEntity team);

    @Query("SELECT j FROM TeamJoinEntity j WHERE j.user IN :users AND joinStatus = 'ACTIVE' AND j.active = true")
    Collection<TeamJoinEntity> findActiveByUsers(Collection<UserEntity> users);

}
