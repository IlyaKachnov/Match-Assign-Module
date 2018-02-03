package app.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 40)
    private String name;

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

    public Team(String name, League league) {
        this.name = name;
        this.league = league;
    }

    public Team() {
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "league_id", nullable = false)
    private League league;
    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
    }


    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "team_user",
            joinColumns = {@JoinColumn(name = "team_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
            )
    @PrimaryKeyJoinColumn
    private List<User> users;
    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @OneToMany(targetEntity = Match.class, fetch = FetchType.LAZY,
            cascade =  CascadeType.ALL,
            mappedBy = "homeTeam")
    private List<Match> matchesAsHome;
    public List<Match> getMatchesAsHome() {
        return matchesAsHome;
    }

    public void setMatchesAsHome(List<Match> matchesAsHome) {
        this.matchesAsHome = matchesAsHome;
    }

    @OneToMany(targetEntity = Match.class, fetch = FetchType.LAZY,
            cascade =  CascadeType.ALL,
            mappedBy = "guestTeam")
    private List<Match> matchesAsGuest;
    public List<Match> getMatchAsGuest() {
        return matchesAsGuest;
    }

    public void setMatchAsGuest(List<Match> matchesAsGuest) {
        this.matchesAsGuest = matchesAsGuest;
    }
}
