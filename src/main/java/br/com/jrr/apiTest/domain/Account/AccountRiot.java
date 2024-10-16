package br.com.jrr.apiTest.domain.Account;


import br.com.jrr.apiTest.User.UserEntity;
import br.com.jrr.apiTest.domain.API.DataAccountAPI;
import br.com.jrr.apiTest.domain.DTO.DadosUpdateDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;


import java.util.List;

@Table(name= "accounst_riot")
@Entity(name = "accountRiots")
@Getter
@AllArgsConstructor
@EqualsAndHashCode(of = "id")

public class AccountRiot {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotNull
    private String puuid;

    @NotNull
    private String gameName;

    @NotNull
    private String tagLine;

    @ElementCollection
    private List<String> idMatchList;

    @NotNull
    private String accountId;

    @NotNull
    private String idRiot;

    @NotNull
    private String profileIconId;

    @NotNull
    private String revisionDate;

    @NotNull
    private String summonerLevel;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public AccountRiot(DataAccountAPI data1, DataAccountAPI data2) {
        this.puuid = data1.puuid();
        this.gameName = data1.gameName();
        this.tagLine = data1.tagLine();
        this.accountId = data2.accountId();
        this.idRiot = data2.idRiot();
        this.profileIconId = data2.profileIconId();
        this.revisionDate = data2.revisionDate();
        this.summonerLevel = data2.summonerLevel();
    }





    public void addIdMatches(List<String> idMatches) {
        this.idMatchList.addAll(idMatches);
    }

    public AccountRiot() {

    }
    public void setIdMatchList(List<String> idMatchList) {
        this.idMatchList = idMatchList;
    }


    public void UpdateAccountDTO(DadosUpdateDTO dados) {
        if(dados.gameName() != null){
            this.gameName = dados.gameName();
        }
        if(dados.tagLine() != null){
            this.tagLine = dados.tagLine();
        }

    }


    public void setUser(UserEntity user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "AccountRiot{" +
                "id=" + id +
                ", puuid='" + puuid + '\'' +
                ", gameName='" + gameName + '\'' +
                ", tagLine='" + tagLine + '\'' +
                ", idMatchList=" + idMatchList +
                '}';
    }

}
