package app.services;

import app.models.Slot;
import app.models.SlotType;
import app.models.Stadium;
import app.repositories.SlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.Date;
import java.util.List;

@Service
@Primary
public class SlotServiceImpl implements SlotService {

    @Autowired
    SlotRepository slotRepository;

    @Override
    public Slot findById(Long id) {
        return this.slotRepository.findById(id).orElse(null);
    }

    @Override
    public List<Slot> findAll() {
        return this.slotRepository.findAll();
    }

    @Override
    public void save(Slot slot) {
        this.slotRepository.save(slot);
    }

    @Override
    public void delete(Long id) {
        this.slotRepository.deleteById(id);
    }

    public void createAndSaveSlots(Slot slot, Stadium stadium) {
        LocalTime startSlotTime = Instant.ofEpochMilli(slot.getStartTime().getTime())
                .atZone(ZoneId.systemDefault()).toLocalTime();
        LocalTime endSlotTime = Instant.ofEpochMilli(slot.getEndTime().getTime())
                .atZone(ZoneId.systemDefault()).toLocalTime();

        LocalDate slotDate = Instant.ofEpochMilli(slot.getEventDate().getTime())
                .atZone(ZoneId.systemDefault()).toLocalDate();

        LocalDateTime startDateTime = LocalDateTime.of(slotDate, startSlotTime);
        LocalDateTime endDateTime = LocalDateTime.of(slotDate, endSlotTime);

        int durationHours = slot.getSlotType().getDuration().getHours();
        int durationMinutes = slot.getSlotType().getDuration().getMinutes();

        while (checkTime(startDateTime, endDateTime, durationHours, durationMinutes)) {
            Slot newSlot = new Slot();
            newSlot.setStartTime(Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant()));
            newSlot.setEventDate(slot.getEventDate());
            newSlot.setStadium(stadium);
            newSlot.setSlotType(slot.getSlotType());

            startDateTime = startDateTime.plusHours(durationHours).plusMinutes(durationMinutes);

            newSlot.setEndTime(Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant()));
            save(newSlot);
        }
    }

    private boolean checkTime(LocalDateTime startDateTime, LocalDateTime endDateTime,
                              int durationHours, int durationMinutes) {
        boolean isValidEndTime = startDateTime.isBefore(endDateTime) &&
                (!startDateTime.plusHours(durationHours).plusMinutes(durationMinutes).isAfter(endDateTime));
        return isValidEndTime;
    }
}
