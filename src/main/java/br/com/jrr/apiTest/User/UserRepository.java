package br.com.jrr.apiTest.User;

import java.util.UUID;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    
    Optional<UserEntity> findByEmail(String email);

    @Query(value = "SELECT u FROM UserEntity u WHERE u.nickname LIKE %:nickname%")
    Page<UserEntity> findByNickname(@Param("nickname") String nickname, Pageable pageable);

    Optional<UserEntity> findByNickname(String nickname);
    Optional<UserEntity> findByCpf(String cpf);

}
