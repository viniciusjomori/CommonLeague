package br.com.jrr.apiTest.User;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import java.time.LocalDate;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import br.com.jrr.apiTest.RiotAccount.RiotAccEntity;
import br.com.jrr.apiTest.Security.TokenEntity;
import br.com.jrr.apiTest.Security.UserDetailsAdapter;
import br.com.jrr.apiTest.Team.Entity.TeamJoinEntity;
import br.com.jrr.apiTest.User.Enum.UserType;
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
@Table(name = "users_p2")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends UserDetailsAdapter {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id", columnDefinition = "VARCHAR(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;

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

    @Column()
    private String imagePath;

    @Column()
    private String pixKey;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserType role;
    
    @OneToMany(mappedBy = "user")
    private Collection<RiotAccEntity> riotAccounts;
    
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
