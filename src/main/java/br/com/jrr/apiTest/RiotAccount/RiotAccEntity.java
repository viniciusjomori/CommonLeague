package br.com.jrr.apiTest.RiotAccount;

import br.com.jrr.apiTest.App.BaseEntity;
import br.com.jrr.apiTest.User.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "riot_account")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class RiotAccEntity extends BaseEntity {
    
    @Column
    private String gameName;

    @Column
    private String tagLine;

    @Column
    private String puuid;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

}
