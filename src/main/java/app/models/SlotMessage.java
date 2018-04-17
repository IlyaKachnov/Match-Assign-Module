package app.models;

import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "slot_messages")
public class SlotMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message", nullable = false)
    @Type(type = "text")
    private String message;

    @OneToOne
    @JoinColumn(name = "match_id")
    private Match match;


    @Column(nullable = false, columnDefinition = "TINYINT NOT NULL", length = 1)
    private Boolean isConsidered;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public Boolean getConsidered() {
        return isConsidered;
    }

    public void setConsidered(Boolean considered) {
        isConsidered = considered;
    }

    public SlotMessage() {
    }
}
