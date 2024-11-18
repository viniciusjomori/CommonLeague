package br.com.jrr.apiTest.RiotAccount;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class RiotAccountScheduller {

    @Autowired
    private RiotAccService service;

    @Autowired
    private RiotAccRepository repository;

    @Scheduled(cron = "0 0 0 * * ?")
    public void updateAllSummonerInfo() {
        Collection<RiotAccEntity> accounts = repository.findAllWithoutSummonerInfo();
        for (RiotAccEntity account : accounts) {
            account = service.updateSummonerInfo(account);
            repository.save(account);
        }
    }
}
