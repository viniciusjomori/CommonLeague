package br.com.jrr.apiTest.Chip.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.jrr.apiTest.Chip.Entity.InventoryEntity;
import br.com.jrr.apiTest.Chip.Entity.ChipEntity;
import br.com.jrr.apiTest.User.UserEntity;

public interface InventoryRepository extends JpaRepository<InventoryEntity, UUID>{
    
    Collection<InventoryEntity> findByUser(UserEntity user);

    @Query("SELECT i FROM InventoryEntity i WHERE i.user = :user AND i.chip = :chip")
    Optional<InventoryEntity> findByUser(UserEntity user, ChipEntity chip);

}
