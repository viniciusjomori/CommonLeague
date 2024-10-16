package br.com.jrr.apiTest.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRiotRepository extends JpaRepository<AccountRiot, String> {

    AccountRiot findByPuuid(String puuid);
}
