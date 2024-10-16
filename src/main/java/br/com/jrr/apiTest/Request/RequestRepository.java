package br.com.jrr.apiTest.Request;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<RequestEntity, UUID> {
    
}
