package br.com.jrr.apiTest.Transaction;

import java.util.Collection;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.jrr.apiTest.Chip.Entity.InventoryEntity;

public interface TransactionRepository extends JpaRepository<TransactionEntity, UUID> {
    
    @Query("SELECT t FROM TransactionEntity t WHERE inventory IN :inventories")
    Page<TransactionEntity> findAllByInventories(Collection<InventoryEntity> inventories, PageRequest pageable);

}
