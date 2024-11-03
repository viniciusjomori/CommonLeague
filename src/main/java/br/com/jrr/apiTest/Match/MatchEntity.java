package br.com.jrr.apiTest.Match;

import java.util.Collection;

import br.com.jrr.apiTest.App.BaseEntity;
import br.com.jrr.apiTest.Tournament.Entity.TournamentJoinEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "matchs")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MatchEntity extends BaseEntity {
    
    @ManyToMany(mappedBy = "matches")
    private Collection<TournamentJoinEntity> joins;

    private String idRiot;

}
