package app.dto;

import app.models.Match;

import java.text.SimpleDateFormat;

public class MatchDTO {
    private String home;
    private String guest;
    private String league;
    private String tour;
    private String formattedDate;
    private String status;
    private String delayed;

//    public MatchDTO(Match match) {
//        this.home = match.getHomeTeam().getName();
//        this.guest = match.getGuestTeam().getName();
//        this.league = match.getTour().getLeague().getName();
//        this.tour = match.getTour().getFullInfo();
//        this.formattedDate = match.getMatchDate()
//        if (matchDate == null) {
//            return "Не назначен";
//        }
//
//        return (new SimpleDateFormat("dd.MM.YYYY")).format(matchDate);
//        this.status = match.getSlot() != null ? match.getSlot().getStadium().getName() : "Не назначен";
//        this.delayed = match.getDelayed() ? "Да" : "Нет";
//    }

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

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public String getTour() {
        return tour;
    }

    public void setTour(String tour) {
        this.tour = tour;
    }

    public String getFormattedDate() {
        return formattedDate;
    }

    public void setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDelayed() {
        return delayed;
    }

    public void setDelayed(String delayed) {
        this.delayed = delayed;
    }
}
