package app.dto;

import app.models.SlotSignificationTime;
import java.time.*;

public class NotificationDTO {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String league;
    private boolean isActual;

    public NotificationDTO(SlotSignificationTime significationTime) {

        LocalDate startLocalDate = Instant.ofEpochMilli(significationTime.getStartDate().getTime())
                .atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endLocalDate = Instant.ofEpochMilli(significationTime.getEndDate().getTime())
                .atZone(ZoneId.systemDefault()).toLocalDate();
        LocalTime startLocalTime = Instant.ofEpochMilli(significationTime.getStartTime().getTime())
                .atZone(ZoneId.systemDefault()).toLocalTime();
        LocalTime endLocalTime = Instant.ofEpochMilli(significationTime.getEndTime().getTime())
                .atZone(ZoneId.systemDefault()).toLocalTime();
        this.startTime = LocalDateTime.of(startLocalDate, startLocalTime);
        this.endTime = LocalDateTime.of(endLocalDate, endLocalTime);
    }

    public void setActual(boolean actual) {
        isActual = actual;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
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
