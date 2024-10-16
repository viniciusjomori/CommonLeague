package br.com.jrr.apiTest.App;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private Boolean active;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createDate;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime editDate;

    @PrePersist
    public void onPersist() {
        this.setCreateDate(LocalDateTime.now());
        this.setActive(true);
    } 

    @PreUpdate
    public void onUpdate() {
        this.setEditDate(LocalDateTime.now());
    }

}