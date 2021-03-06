package app.models;

import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "leagues", schema = "public")
public class League {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 40)
    private String name;

    @OneToMany(targetEntity = Team.class, mappedBy = "league", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Team> teams;

    @OneToMany(targetEntity = Tour.class, mappedBy = "league", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Tour> tours;

    public List<Tour> getTours() {
        return tours;
    }

    public void setTours(List<Tour> tours) {
        this.tours = tours;
    }

    @OneToOne(targetEntity = SlotSignificationTime.class,
            cascade = CascadeType.ALL,
            mappedBy = "league")
    private SlotSignificationTime slotSignificationTime;

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SlotSignificationTime getSlotSignificationTime() {
        return slotSignificationTime;
    }

    public void setSlotSignificationTime(SlotSignificationTime slotSignificationTime) {
        this.slotSignificationTime = slotSignificationTime;
    }

    public League(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public League() {
    }
}
