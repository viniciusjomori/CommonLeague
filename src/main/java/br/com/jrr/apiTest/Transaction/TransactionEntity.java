package br.com.jrr.apiTest.Transaction;

import br.com.jrr.apiTest.App.BaseEntity;
import br.com.jrr.apiTest.Ticket.Entity.InventoryEntity;
import br.com.jrr.apiTest.Transaction.Enum.TransactionStatus;
import br.com.jrr.apiTest.Transaction.Enum.TransactionType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "tickest")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEntity extends BaseEntity {
    
    @Column
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Column
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
    
    @ManyToOne
    @JoinColumn(name = "inventory_id", nullable = false)
    private InventoryEntity inventory;

    @Column()
    @Min(value = 1)
    private int qnt;

}
