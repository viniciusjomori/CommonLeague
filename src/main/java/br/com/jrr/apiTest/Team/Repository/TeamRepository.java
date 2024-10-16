package br.com.jrr.apiTest.Team.Repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.jrr.apiTest.Team.Entity.TeamEntity;

public interface TeamRepository extends JpaRepository<TeamEntity, UUID> {
    
}
