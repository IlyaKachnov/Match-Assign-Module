package app.services;

import app.models.*;
import app.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SlotsSignificationService {

    private static final Logger logger = LoggerFactory.getLogger(SlotsSignificationService.class);
    private final StadiumRepository stadiumRepository;
    private final MatchRepository matchRepository;
    private final SlotRepository slotRepository;
    private final SlotSignificationTimeRepository slotSignificationTimeRepository;
    private final UserRepository userRepository;

    @Autowired
    public SlotsSignificationService(StadiumRepository stadiumRepository,
                                     MatchRepository matchRepository,
                                     SlotRepository slotRepository,
                                     SlotSignificationTimeRepository slotSignificationTimeRepository,
                                     UserRepository userRepository) {
        this.stadiumRepository = stadiumRepository;
        this.matchRepository = matchRepository;
        this.slotRepository = slotRepository;
        this.slotSignificationTimeRepository = slotSignificationTimeRepository;
        this.userRepository = userRepository;
    }

    public String generateSlotsJSON(Long id, String userEmail) {
        List<Slot> slots;
        try {
            slots = stadiumRepository.findOne(id).getSlots();
            if (slots.isEmpty()) {
                return null;
            }
        } catch (NullPointerException exc) {
            logger.info("slots not found");
            return null;
        }

        List<SlotSignificationTime> slotSignificationTimes = slotSignificationTimeRepository.findAll();
        User currUser = userRepository.findByEmail(userEmail);
        Set<League> userLeagues = new HashSet<>();
        List<Team> userTeams = currUser.getTeamList();
        if (userTeams.isEmpty()) {
            return null;
        }
        currUser.getTeamList().forEach(team -> {
            userLeagues.add(team.getLeague());
        });

        List<League> activeLeagues = new ArrayList<>();
        addActiveLeagues(activeLeagues, slotSignificationTimes, userLeagues);
        return generateJSON(activeLeagues, slots, userEmail, id);
    }

    public List<Match> getActualMatches(HttpServletRequest httpServletRequest,
                                                        Long slotId) {
        User currUser = userRepository.findByEmail(httpServletRequest.getUserPrincipal().getName());
        List<Team> userTeams = currUser.getTeamList();
        Slot currSlot = slotRepository.findOne(slotId);

        if (currUser.getRole().equals(Role.adminRole)) {
            List<Match> allMatchesByDate = matchRepository.findAll().stream()
                    .filter(match -> match.getMatchDate().equals(currSlot.getEventDate()))
                    .collect(Collectors.toList());
            return allMatchesByDate;
        }
        List<Match> actualMatches = new ArrayList<>();
        List<SlotSignificationTime> slotSignificationTimes = slotSignificationTimeRepository.findAll();
        List<League> activeLeagues = new ArrayList<>();
        addActiveLeagues(activeLeagues, slotSignificationTimes, getUserLeagues(currUser));

        userTeams.stream().filter(team -> activeLeagues.contains(team.getLeague()))
                .forEach(team -> {
                    actualMatches.addAll(matchRepository.findByHomeTeam(team).stream()
                            .filter(match -> match.getMatchDate().equals(currSlot.getEventDate())
                                    && match.getSlot() == null)
                            .collect(Collectors.toList()));
        });
        return actualMatches;
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

    private void addActiveLeagues(List<League> activeLeagues,
                                  List<SlotSignificationTime> slotSignificationTimes,
                                  Set<League> userLeagues) {
        if (slotSignificationTimes == null || slotSignificationTimes.isEmpty()) {
            return;
        }
        slotSignificationTimes.forEach(slotSignificationTime -> {
            if (userLeagues.contains(slotSignificationTime.getLeague())
                    && checkDateTime(slotSignificationTime)) {
                activeLeagues.add(slotSignificationTime.getLeague());
            }
        });
    }

    private Set<League> getUserLeagues(User currUser) {
        Set<League> userLeagues = new HashSet<>();
        List<Team> userTeams = currUser.getTeamList();
        if (userTeams.isEmpty()) {
            return null;
        }
        currUser.getTeamList().forEach(team -> {
            userLeagues.add(team.getLeague());
        });
        return userLeagues;
    }

    private boolean checkDateTime(SlotSignificationTime slotSignificationTime) {
        LocalDateTime currDateTime = LocalDateTime.now();
        LocalTime startTime = Instant.ofEpochMilli(slotSignificationTime.getStartTime().getTime())
                .atZone(ZoneId.systemDefault()).toLocalTime();
        LocalDate startDate = Instant.ofEpochMilli(slotSignificationTime.getStartDate().getTime())
                .atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);
        LocalTime endTime = Instant.ofEpochMilli(slotSignificationTime.getEndTime().getTime())
                .atZone(ZoneId.systemDefault()).toLocalTime();
        LocalDate endDate = Instant.ofEpochMilli(slotSignificationTime.getEndDate().getTime())
                .atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);
        return currDateTime.isAfter(startDateTime) &&
                currDateTime.isBefore(endDateTime);
    }

    private String generateJSON(List<League> leagues, List<Slot> slots, String userEmail, Long id) {
        User currUser = userRepository.findByEmail(userEmail);
        StringBuilder slotsJSON = new StringBuilder("[");
        slots.forEach(slot -> {
            slotsJSON.append("{\"title\": \"").append(slot.getEventName()).append("\",");
            slotsJSON.append("\"start\": \"").append(slot.getEventDate())
                    .append(" ").append(slot.getStartTime()).append("\",");
            slotsJSON.append("\"end\": \"").append(slot.getEventDate())
                    .append(" ").append(slot.getEndTime()).append("\",");
            if (slot.getMatch() != null) {
                if ((slot.getMatch().getHomeTeam().getUser().getEmail().equals(userEmail) &&
                        leagues.contains(slot.getMatch().getHomeTeam().getLeague())) ||
                        currUser.getRole().equals(Role.adminRole)) {
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
            } else if (!leagues.isEmpty() || currUser.getRole().equals(Role.adminRole)) {
                slotsJSON.append("\"description\": \"<a href='/stadium/")
                        .append(id).append("/signify/").append(slot.getId())
                        .append("'>Занять слот</a>\"},");
            } else {
                slotsJSON.deleteCharAt(slotsJSON.lastIndexOf(","));
                slotsJSON.append("},");
            }
        });
        slotsJSON.deleteCharAt(slotsJSON.lastIndexOf(","));
        slotsJSON.append("]");
        return slotsJSON.toString();
    }
}
