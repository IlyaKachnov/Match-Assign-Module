package app.models;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users", schema = "public")
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

    @Column(name = "reset_token")
    private String resetToken;

    @OneToMany(targetEntity = Team.class, mappedBy = "user", fetch = FetchType.LAZY)
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public User() {
    }

}
