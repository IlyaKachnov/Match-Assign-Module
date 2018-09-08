package app.models;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "match_messages")
public class MatchMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "message", nullable = false)
    @Type(type = "text")
    private String message;

    @OneToOne
    @MapsId
    @JoinColumn(name = "match_id")
    private Match match;


    @Column(nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
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

    public MatchMessage() {
    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        MatchMessage that = (MatchMessage) o;
//        return Objects.equals(message, that.message) &&
//                Objects.equals(match, that.match) &&
//                Objects.equals(isConsidered, that.isConsidered);
//    }
//
//    @Override
//    public int hashCode() {
//
//        return Objects.hash(message, match, isConsidered);
//    }
}
