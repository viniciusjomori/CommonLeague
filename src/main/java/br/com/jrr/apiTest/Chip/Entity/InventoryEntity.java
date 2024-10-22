package br.com.jrr.apiTest.Chip.Entity;

import br.com.jrr.apiTest.App.BaseEntity;
import br.com.jrr.apiTest.User.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "inventory")
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryEntity extends BaseEntity {
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "chip_id", nullable = false)
    private ChipEntity chip;

    @Column
    @Min(value = 0)
    private int qnt;

    public void plusQnt(int qnt) {
        this.qnt += qnt;
    }

}