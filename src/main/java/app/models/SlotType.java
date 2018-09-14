package app.models;

import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "slot_types", schema = "public",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"typeName", "duration"})})
public class SlotType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String typeName;

    @Column(nullable = false)
    @Temporal(TemporalType.TIME)
    @DateTimeFormat(pattern = "HH:mm")
    private Date duration;

    @Column(nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean isSignifiable;

    @OneToMany(targetEntity = Slot.class, mappedBy = "slotType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Slot> slots;

    public List<Slot> getSlots() {
        return slots;
    }

    public void setSlots(List<Slot> slots) {
        this.slots = slots;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Date getDuration() {
        return duration;
    }

    public String getFormattedDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.duration);
        if (calendar.get(Calendar.HOUR) == 0) {
            return calendar.get(Calendar.MINUTE) + " мин";
        }

        return calendar.get(Calendar.HOUR) + " ч " + calendar.get(Calendar.MINUTE) + " мин";
    }

    public String getFullName() {
        return this.typeName + " - " + getFormattedDate();
    }

    public void setDuration(Date duration) {
        this.duration = duration;
    }

    public Boolean getSignifiable() {
        return isSignifiable;
    }

    public void setSignifiable(Boolean signifiable) {
        isSignifiable = signifiable;
    }

    public SlotType() {
    }

    public Long getId() {
        return id;
    }
}
