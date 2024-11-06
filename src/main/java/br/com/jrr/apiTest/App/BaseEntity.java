package br.com.jrr.apiTest.App;

import java.time.LocalDateTime;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public abstract class BaseEntity {

    private Boolean active;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime creationDate;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updateDate;

    @PrePersist
    public void onPersist() {
        this.setCreationDate(LocalDateTime.now());
        this.setActive(true);
    } 

    @PreUpdate
    public void onUpdate() {
        this.setUpdateDate(LocalDateTime.now());
    }

}