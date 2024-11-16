package br.com.jrr.apiTest.Tournament;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.jrr.apiTest.Tournament.Entity.TournamentEntity;
import br.com.jrr.apiTest.Tournament.Enum.TournamentStatus;
import br.com.jrr.apiTest.Tournament.Repository.TournamentRepository;

@Component
@EnableScheduling
public class TournamentScheduler {
    
    @Autowired
    private TournamentService service;

    @Autowired
    private TournamentRepository repository;

    @Scheduled(cron = "0 0 * * * ?")
    public void createTournament() {
        Collection<TournamentEntity> pendingTournament = repository.findAllByStatus(TournamentStatus.PENDING);
        for (TournamentEntity tournament : pendingTournament) {
            service.startTournament(tournament);
        }
    }

}
