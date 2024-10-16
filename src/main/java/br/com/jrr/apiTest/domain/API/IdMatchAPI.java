package br.com.jrr.apiTest.domain.API;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class IdMatchAPI {

    @JsonProperty("idMatch")
    private List<String> idMatch;

    public void setIdMatch(List<String> idMatch) {
        this.idMatch = idMatch;
    }
}
