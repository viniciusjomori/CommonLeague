package br.com.jrr.apiTest.Team.Entity;

import java.util.Collection;

import br.com.jrr.apiTest.App.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "teams")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TeamEntity extends BaseEntity {
    
    @Column(unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "team")
    private Collection<TeamJoinEntity> joins;

}
