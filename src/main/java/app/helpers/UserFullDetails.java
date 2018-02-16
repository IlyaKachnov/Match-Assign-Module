package app.helpers;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class UserFullDetails extends User {
    private String firstname;
    private String lastname;
    private String role;
    private Long id;
    private String fullName;

    public UserFullDetails(String username, String password, Collection<? extends GrantedAuthority> authorities,
                           String firstname, String lastname, String role, Long id)
    {
        super(username, password, true, true, true, true, authorities);
        this.firstname = firstname;
        this.lastname = lastname;
        this.role = role;
        this.id = id;
        this.fullName = firstname + " " + lastname;
    }


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }
}
