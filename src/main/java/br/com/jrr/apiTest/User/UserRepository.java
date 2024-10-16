package br.com.jrr.apiTest.User;

import java.util.UUID;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    
    Optional<UserEntity> findByEmail(String email);

}
