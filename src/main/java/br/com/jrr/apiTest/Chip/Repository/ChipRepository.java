package br.com.jrr.apiTest.Chip.Repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.jrr.apiTest.Chip.Entity.ChipEntity;

public interface ChipRepository extends JpaRepository<ChipEntity, UUID> {
    
}
