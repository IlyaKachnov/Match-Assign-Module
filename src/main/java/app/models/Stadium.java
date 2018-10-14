package app.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "stadiums", schema = "public")
public class Stadium {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, unique = true, nullable = false)
    private String name;

    @OneToMany(targetEntity = Slot.class, mappedBy = "stadium",
            cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @org.hibernate.annotations.OrderBy(clause = "event_date desc ")
    private List<Slot> slots;

    public List<Slot> getSlots() {
        return slots;
    }

    public void setSlots(List<Slot> slots) {
        this.slots = slots;
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

    public Stadium() {
    }
}
