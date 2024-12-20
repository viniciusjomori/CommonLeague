package br.com.jrr.apiTest.Team.Entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import br.com.jrr.apiTest.App.BaseEntity;
import br.com.jrr.apiTest.Team.Enum.TeamJoinStatus;
import br.com.jrr.apiTest.Team.Enum.TeamRoleName;
import br.com.jrr.apiTest.User.UserEntity;
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
@Table(name = "join_team")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TeamJoinEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private TeamEntity team;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TeamRoleName role;

    @Column()
    private LocalDateTime responseDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TeamJoinStatus joinStatus;

    @Column
    private boolean openToPlay;

}
