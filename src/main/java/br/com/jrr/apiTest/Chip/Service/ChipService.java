package br.com.jrr.apiTest.Chip.Service;

import java.util.Collection;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jrr.apiTest.App.Exceptions.NotFoundException;
import br.com.jrr.apiTest.Chip.Entity.ChipEntity;
import br.com.jrr.apiTest.Chip.Repository.ChipRepository;
@Service
public class ChipService {
    
    @Autowired
    private ChipRepository repository;

    public Collection<ChipEntity> findAll() {
        return repository.findAll();
    }

    public ChipEntity findById(UUID id) {
        return repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Chip not found"));
    }

    public ChipEntity getCommomCoin() {
        return repository
            .findByDescription("Common Coin")
            .get();
    }

}
