package app.models;

import javax.persistence.*;

@Entity
@Table(name="stadiums")
public class Stadium {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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

    public Stadium() {
    }

    public Stadium(String name) {
        this.name = name;
    }
}
