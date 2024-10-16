package br.com.jrr.apiTest.Ticket.Entity;

import br.com.jrr.apiTest.App.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;
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
public class TicketEntity extends BaseEntity {
    
    @Column(nullable = false, unique = true)
    private String description;

    @Positive
    @Column(nullable = false)
    private double value;

}
