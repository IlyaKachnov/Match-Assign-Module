package app.services;

import app.models.Match;
import app.models.Slot;
import app.repositories.MatchRepository;
import app.repositories.SlotRepository;
import app.repositories.StadiumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SlotsSignificationService {

    private final StadiumRepository stadiumRepository;
    private final MatchRepository matchRepository;
    private final SlotRepository slotRepository;

    @Autowired
    public SlotsSignificationService(StadiumRepository stadiumRepository,
                                     MatchRepository matchRepository,
                                     SlotRepository slotRepository) {
        this.stadiumRepository = stadiumRepository;
        this.matchRepository = matchRepository;
        this.slotRepository = slotRepository;
    }

    public String generateSlotsJSON(Long id, String userEmail) {
        List<Slot> slots = stadiumRepository.findOne(id).getSlots();
        StringBuilder slotsJSON = new StringBuilder("[");
        slots.forEach(slot -> {
            slotsJSON.append("{\"title\": \"").append(slot.getEventName()).append("\",");
            slotsJSON.append("\"start\": \"").append(slot.getEventDate())
                    .append(" ").append(slot.getStartTime()).append("\",");
            slotsJSON.append("\"end\": \"").append(slot.getEventDate())
                    .append(" ").append(slot.getEndTime()).append("\",");
            if (slot.getMatch() != null) {
                if (slot.getMatch().getHomeTeam().getUser().getEmail().equals(userEmail)) {
                    slotsJSON.append("\"description\": \"")
                            .append(slot.getMatch().getHomeTeam().getName())
                            .append(" - ")
                            .append(slot.getMatch().getGuestTeam().getName())
                            .append(" <a href='/stadium/").append(id).append("/reject/").append(slot.getId()).append("'>Отменить</a>")
                            .append("\"},");
                } else {
                    slotsJSON.append("\"description\": \"")
                            .append(slot.getMatch().getHomeTeam().getName())
                            .append(" - ")
                            .append(slot.getMatch().getGuestTeam().getName())
                            .append("\"},");
                }
            } else {
                slotsJSON.append("\"description\": \"<a href='/stadium/")
                        .append(id).append("/signify/").append(slot.getId())
                        .append("'>Занять слот</a>\"},");
            }
        });
        slotsJSON.deleteCharAt(slotsJSON.lastIndexOf(","));
        slotsJSON.append("]");
        return slotsJSON.toString();
    }

    public void signifySlot(Long matchId, Long slotId) {
        Match match = matchRepository.findOne(matchId);
        Slot currSlot = slotRepository.findOne(slotId);
        currSlot.setMatch(match);
        currSlot.setEventName("Слот занят");
        slotRepository.save(currSlot);
    }

    public void rejectSlot(Long slotId) {
        Slot currSlot = slotRepository.findOne(slotId);
        currSlot.setMatch(null);
        currSlot.setEventName("Пустой слот");
        slotRepository.save(currSlot);
    }
}
