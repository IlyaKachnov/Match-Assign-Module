package app.models;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 60)
    private String firstname;

    @Column(length = 60)
    private String lastname;

    @Column(nullable = false, unique = true, length = 60)
    private String email;

    @Column(length = 20)
    private String role;

    @Column(length = 60)
    private String password;

    @OneToMany(targetEntity = Team.class, mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    private List<Team> teams;
    public List<Team> getTeamList() {
        return teams;
    }

    public void setTeamList(List<Team> teamList) {
        this.teams = teamList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {

        this.password = password;
    }

    public User(Long id, String firstname, String lastname, String email, String role, String password) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.role = role;
        this.password  = password;
    }

    public User() {
    }

}
