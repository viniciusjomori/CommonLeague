package br.com.jrr.apiTest.Tournament.Entity;

import java.time.LocalDateTime;
import java.util.Collection;

import br.com.jrr.apiTest.App.BaseEntity;
import br.com.jrr.apiTest.Tournament.Enum.TournamentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
