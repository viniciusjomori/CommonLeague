package br.com.jrr.apiTest.Match;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import br.com.jrr.apiTest.App.BaseEntity;
import br.com.jrr.apiTest.Match.Enums.MatchStatus;
import br.com.jrr.apiTest.Tournament.Entity.TournamentJoinEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "match_id", columnDefinition = "VARCHAR(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;

    @Column
    @Enumerated(EnumType.STRING)
    private MatchStatus status;

    @ManyToOne
    @JoinColumn(name = "join_1_id", nullable = false)
    private TournamentJoinEntity join1;

    @ManyToOne
    @JoinColumn(name = "join_2_id", nullable = false)
    private TournamentJoinEntity join2;

    @Column
    private LocalDateTime startDate;

    @Column
    private String riotId;

    public TournamentJoinEntity getWinner() {
        if (this.status.equals(MatchStatus.PENDING))
            return null;
        
        return this.status.equals(MatchStatus.TEAM_1_WINS) ? this.join1 : this.join2;
    }

    public TournamentJoinEntity getLoser() {
        if (this.status.equals(MatchStatus.PENDING))
            return null;
        
        return this.status.equals(MatchStatus.TEAM_1_WINS) ? this.join2 : this.join1;
    }

}
