package app.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "matches")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(targetEntity = Slot.class, mappedBy = "match")
    private Slot slot;

    @OneToOne(targetEntity = SlotMessage.class, mappedBy = "match", cascade = CascadeType.ALL)
    private SlotMessage slotMessage;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date matchDate;

    @Column(nullable = false, columnDefinition = "TINYINT NOT NULL", length = 1)
    private Boolean isDelayed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_id", nullable = false)
    private Team homeTeam;

    public Team getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_id", nullable = false)
    private Team guestTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_id", nullable = false)
    private Tour tour;

    public Tour getTour() {
        return tour;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    public Team getGuestTeam() {
        return guestTeam;
    }

    public void setGuestTeam(Team guestTeam) {
        this.guestTeam = guestTeam;
    }

    public Slot getSlot() {
        return slot;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getMatchDate() {
        return matchDate;
    }

    public String getFormattedDate() {

        if (matchDate == null) {
            return "Не назначен";
        }

        return (new SimpleDateFormat("dd.MM.YYYY")).format(matchDate);
    }

    public void setMatchDate(Date matchDate) {
        this.matchDate = matchDate;
    }

    public Boolean getDelayed() {
        return isDelayed;
    }

    public void setDelayed(Boolean delayed) {
        isDelayed = delayed;
    }

    public SlotMessage getSlotMessage() {
        return slotMessage;
    }

    public void setSlotMessage(SlotMessage slotMessage) {
        this.slotMessage = slotMessage;
    }

    public Match() {
    }

    public String getHomeAndGuest(){
        return homeTeam.getName() + " - " +guestTeam.getName();
    }
}
