package app.dto;

import app.models.League;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class LeagueFilterDTO {
    private String title;

    public LeagueFilterDTO(League league) {
        this.title = league.getName();
    }

    public static Set<LeagueFilterDTO> createLeagueList(List<League> leagues){
        return leagues.stream().map(league -> new LeagueFilterDTO(league)).collect(Collectors.toSet());
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
