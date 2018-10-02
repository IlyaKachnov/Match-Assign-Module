package app.dto;

import app.models.League;
import app.models.SlotSignificationTime;

import java.text.SimpleDateFormat;

public class NotificationDTO {
    private String startTime;
    private String endTime;
    private String league;
    private boolean isActual;

    public NotificationDTO(SlotSignificationTime significationTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        this.startTime = timeFormat.format(significationTime.getStartTime()) + " "
                + dateFormat.format(significationTime.getStartDate());
        this.endTime = timeFormat.format(significationTime.getEndTime()) + " "
                + dateFormat.format(significationTime.getEndDate());
    }

    public void setActual(boolean actual) {
        isActual = actual;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean isActual() {
        return isActual;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }
}
