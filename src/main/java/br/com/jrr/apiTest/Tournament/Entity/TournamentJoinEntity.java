package br.com.jrr.apiTest.Tournament.Entity;

import java.util.Collection;

import br.com.jrr.apiTest.App.BaseEntity;
import br.com.jrr.apiTest.Match.MatchEntity;
import br.com.jrr.apiTest.Team.Entity.TeamEntity;
import br.com.jrr.apiTest.Tournament.Enum.TournamentJoinStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "join_tournament")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TournamentJoinEntity extends BaseEntity {
    
    @ManyToOne
    @JoinColumn(name = "tournament_id", nullable = false)
    private TournamentEntity tournament;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private TeamEntity team;

    @Column
    @Enumerated(EnumType.STRING)
    private TournamentJoinStatus status;

    @ManyToMany
    @JoinTable(
        name = "tournament_match",
        joinColumns = @JoinColumn(name = "join_id"),
        inverseJoinColumns = @JoinColumn(name = "match_id")
    )
    private Collection<MatchEntity> matches;

}