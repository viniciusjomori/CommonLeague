package br.com.jrr.apiTest.Ticket.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.jrr.apiTest.Ticket.Entity.InventoryEntity;
import br.com.jrr.apiTest.Ticket.Entity.TicketEntity;
import br.com.jrr.apiTest.User.UserEntity;

public interface InventoryRepository extends JpaRepository<InventoryEntity, UUID>{
    
    Collection<InventoryEntity> findByUser(UserEntity user);

    @Query("SELECT i FROM InventoryEntity i WHERE i.user = :user AND i.ticket = :ticket")
    Optional<InventoryEntity> findByUser(UserEntity user, TicketEntity ticket);

}
