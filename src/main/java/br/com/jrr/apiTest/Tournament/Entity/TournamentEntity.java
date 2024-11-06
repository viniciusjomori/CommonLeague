package br.com.jrr.apiTest.Tournament.Entity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import br.com.jrr.apiTest.App.BaseEntity;
import br.com.jrr.apiTest.Tournament.Enum.TournamentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "tournaments")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TournamentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "tournament_id", columnDefinition = "VARCHAR(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;
    
    @Column
    private String description;

    @Column
    private String riotId;

    @Column
    @Enumerated(EnumType.STRING)
    private TournamentStatus status;

    @Column
    private int qntChipsPerPlayer;

    @Column
    private LocalDateTime startDate;

    @OneToMany(mappedBy = "tournament")
    private Collection<TournamentJoinEntity> joins;

}
