package app.dto;

import app.models.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.stream.Collectors;

public class MatchCalendarDTO {
    private String home;
    private String guest;
    private String matchDate;
    private String delayed;
    private String league;
    private String stadium;
    private String tour;

    public MatchCalendarDTO(Match match) {
        this.home = match.getHomeTeam().getName();
        this.guest = match.getGuestTeam().getName();
        this.matchDate = match.getFormattedDate();
        this.delayed = match.getDelayed() ? "Да" : "Нет";
        this.league = match.getTour().getLeague().getName();
        this.tour = match.getTour().getFullInfo();
        this.stadium = (match.getSlot() != null) ? match.getSlot().getStadium().getName() : "Не назначен";
    }

    public static List<MatchCalendarDTO> createMatchCalendarList(List<Match> matches) {

        return matches.stream().map(match -> new MatchCalendarDTO(match)).collect(Collectors.toList());
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getGuest() {
        return guest;
    }

    public void setGuest(String guest) {
        this.guest = guest;
    }

    public String getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(String matchDate) {
        this.matchDate = matchDate;
    }

    public String getDelayed() {
        return delayed;
    }

    public void setDelayed(String delayed) {
        this.delayed = delayed;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public String getStadium() {
        return stadium;
    }

    public void setStadium(String stadium) {
        this.stadium = stadium;
    }

    public String getTour() {
        return tour;
    }

    public void setTour(String tour) {
        this.tour = tour;
    }
}
