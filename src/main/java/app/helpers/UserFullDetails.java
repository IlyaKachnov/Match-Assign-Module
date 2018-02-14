package app.helpers;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class UserFullDetails extends User {
    private String firstname;
    private String lastname;

    public UserFullDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, String firstname, String lastname)
    {
        super(username, password, true, true, true, true, authorities);
        this.firstname = firstname;
        this.lastname = lastname;
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
}
