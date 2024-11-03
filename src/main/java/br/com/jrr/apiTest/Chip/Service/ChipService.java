package br.com.jrr.apiTest.Chip.Service;

import java.util.Collection;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.jrr.apiTest.Chip.Entity.ChipEntity;
import br.com.jrr.apiTest.Chip.Repository.ChipRepository;
@Service
public class ChipService {
    
    @Autowired
    private ChipRepository repository;

    @Value("commonleague-pay.base-endpoint")
    private String baseEndpoint;

    public Collection<ChipEntity> findAll() {
        return repository.findAll();
    }

    public ChipEntity findById(UUID id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public ChipEntity getCommomCoin() {
        return repository
            .findByDescription("Common Coin")
            .get();
    }

}
