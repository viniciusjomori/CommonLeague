package br.com.jrr.apiTest.Security;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

import br.com.jrr.apiTest.App.BaseEntity;
import br.com.jrr.apiTest.User.UserEntity;

@Entity
@Table(name = "tokens")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
public class TokenEntity extends BaseEntity {
    
    @Column(unique = true, nullable = false)
    private String token;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private LocalDateTime expirationDate;

    public boolean isNonExpired() {
        return !LocalDateTime.now().isAfter(expirationDate);
    }
}
