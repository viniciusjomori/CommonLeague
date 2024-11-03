package br.com.jrr.apiTest.Tournament;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class TournamentScheduler {
    
    @Autowired
    private TournamentService service;

    @Scheduled(cron = "0 0 * * * ?")
    public void createTournament() {
        service.startTournaments();
    }

}
