package br.com.jrr.apiTest.User;

import java.util.Collection;
import java.util.Collections;

import java.time.LocalDate;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import br.com.jrr.apiTest.Security.TokenEntity;
import br.com.jrr.apiTest.Security.UserDetailsAdapter;
import br.com.jrr.apiTest.Team.Entity.TeamJoinEntity;
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
@Table(name = "users")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends UserDetailsAdapter {

    @Column(unique = true, nullable = false)
    private String nickname;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String fullName;

    @Column(unique = true, nullable = false)
    private String cpf;
    
    @Column(nullable = false)
    private LocalDate birthday;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserType role;

    @OneToMany(mappedBy = "user")
    private Collection<TokenEntity> tokens;

    @OneToMany(mappedBy = "user")
    private Collection<TeamJoinEntity> teamJoins;

    public Collection<SimpleGrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(this.role.toString()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

}
