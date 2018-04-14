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
        if (userTeams.isEmpty() && !currUser.getRole().equals(Role.adminRole)) {
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
                    .filter(match -> (
                            (match.getMatchDate() == null ||
                            match.getMatchDate().equals(currSlot.getEventDate()))
                            && match.getSlot() == null
                            && consistsWithTour(match, currSlot.getEventDate())
                    ))
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
                            .filter(match ->
                                    (match.getMatchDate() == null ||
                                    match.getMatchDate().equals(currSlot.getEventDate()))
                                    && match.getSlot() == null
                                    && consistsWithTour(match, currSlot.getEventDate())
                            )
                            .collect(Collectors.toList()));
                });
        return actualMatches;
    }

    public synchronized void signifySlot(Long matchId, Long slotId, String userEmail) {
        User currUser = userRepository.findByEmail(userEmail);
        Match match = matchRepository.findOne(matchId);
        if (match != null && match.getSlot() != null) {
            return;
        }
        if (match != null && match.getHomeTeam() != null) {
            Optional<SlotSignificationTime> slotSignificationTimeOptional = slotSignificationTimeRepository.findAll()
                    .stream().filter(slotSignificationTime -> slotSignificationTime.getLeague().equals(match.getHomeTeam().getLeague()))
                    .findAny();
            if (currUser.getRole().equals(Role.adminRole) ||
                    (slotSignificationTimeOptional.isPresent() && checkDateTime(slotSignificationTimeOptional.get()))) {
                Slot currSlot = slotRepository.findOne(slotId);
                if (currSlot.getMatch() == null && currSlot.getSlotType().getSignifiable()) {
                    currSlot.setMatch(match);
                    match.setMatchDate(currSlot.getEventDate());
                }
                slotRepository.save(currSlot);
                matchRepository.save(match);
            }
        }
    }

    public void rejectSlot(Long slotId, String userEmail) {
        Slot currSlot = slotRepository.findOne(slotId);
        Match currMatch = currSlot.getMatch();
        User currUser = userRepository.findByEmail(userEmail);
        if (currUser.getRole().equals(Role.adminRole)) {
            currMatch.setMatchDate(null);
            currSlot.setMatch(null);
            matchRepository.save(currMatch);
            slotRepository.save(currSlot);
        } else if (currMatch != null && currMatch.getHomeTeam() != null) {
            Optional<SlotSignificationTime> slotSignificationTimeOptional = slotSignificationTimeRepository.findAll()
                    .stream().filter(slotSignificationTime -> slotSignificationTime.getLeague().equals(currMatch.getHomeTeam().getLeague()))
                    .findAny();
            if (slotSignificationTimeOptional.isPresent() && checkDateTime(slotSignificationTimeOptional.get())) {
                currMatch.setMatchDate(null);
                currSlot.setMatch(null);
                matchRepository.save(currMatch);
                slotRepository.save(currSlot);
            }
        }
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

    private boolean checkFutureSession(SlotSignificationTime slotSignificationTime) {
        LocalDateTime currDateTime = LocalDateTime.now();
        LocalTime startTime = Instant.ofEpochMilli(slotSignificationTime.getStartTime().getTime())
                .atZone(ZoneId.systemDefault()).toLocalTime();
        LocalDate startDate = Instant.ofEpochMilli(slotSignificationTime.getStartDate().getTime())
                .atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);

        return currDateTime.isBefore(startDateTime);
    }

    public List<SlotSignificationTime> getFutureSessions() {

        List<SlotSignificationTime> allSessions = slotSignificationTimeRepository.findAll();
        List<SlotSignificationTime> futureSessions = new ArrayList<>();
        allSessions.forEach(session -> {
            if (checkFutureSession(session)) {
                futureSessions.add(session);
            }
        });

        return futureSessions;
    }

    public List<SlotSignificationTime> getActualSessions() {

        List<SlotSignificationTime> allSessions = slotSignificationTimeRepository.findAll();
        List<SlotSignificationTime> actualSessions = new ArrayList<>();
        allSessions.forEach(session -> {
            if (checkDateTime(session)) {
                actualSessions.add(session);
            }
        });

        return actualSessions;
    }

    public Set<SlotSignificationTime> getFutureSessions(User user) {

        List<Team> teamList = user.getTeamList();
//        if(teamList.isEmpty())
//        {
//            return null;
//        }
        Set<SlotSignificationTime> futureNotifications = new HashSet<>();
        teamList.forEach(team -> {
            SlotSignificationTime slotSignificationTime = team.getLeague().getSlotSignificationTime();
            if (slotSignificationTime == null) {
                return;
            }
            if (checkFutureSession(slotSignificationTime)) {
                futureNotifications.add(slotSignificationTime);
            }
        });

        return futureNotifications;
    }

    public Set<SlotSignificationTime> getActualSessions(User user) {

        List<Team> teamList = user.getTeamList();
//        if(teamList.isEmpty())
//        {
//            return null;
//        }
        Set<SlotSignificationTime> actualNotifications = new HashSet<>();
        teamList.forEach(team -> {
            SlotSignificationTime slotSignificationTime = team.getLeague().getSlotSignificationTime();
            if (slotSignificationTime == null) {
                return;
            }
            if (checkDateTime(slotSignificationTime)) {
                actualNotifications.add(slotSignificationTime);
            }
        });

        return actualNotifications;
    }

    private String generateJSON(List<League> leagues, List<Slot> slots, String userEmail, Long id) {
        User currUser = userRepository.findByEmail(userEmail);
        StringBuilder slotsJSON = new StringBuilder("[");
        slots.forEach(slot -> {
            slotsJSON.append("{\"title\": \"").append(slot.getSlotType().getTypeName()).append("\",");
            slotsJSON.append("\"start\": \"").append(slot.getEventDate())
                    .append(" ").append(slot.getStartTime()).append("\",");
            slotsJSON.append("\"end\": \"").append(slot.getEventDate())
                    .append(" ").append(slot.getEndTime()).append("\",");
            if (slot.getMatch() != null) {
                if ((slot.getMatch().getHomeTeam().getUser() != null &&
                        slot.getMatch().getHomeTeam().getUser().getEmail().equals(userEmail) &&
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
            } else if (slot.getSlotType().getSignifiable() &&
                    (!leagues.isEmpty() || currUser.getRole().equals(Role.adminRole))) {
                slotsJSON.append("\"description\": \"<a href='/stadium/")
                        .append(id).append("/signify/").append(slot.getId())
                        .append("'>Занять слот</a>\"},");
            } else {
                slotsJSON.append("\"description\": \"\"},");
            }
        });
        slotsJSON.deleteCharAt(slotsJSON.lastIndexOf(","));
        slotsJSON.append("]");
        return slotsJSON.toString();
    }

    private boolean consistsWithTour(Match match, Date eventDate){
        if(match.getDelayed()){
            return true;
        }

        if(eventDate.after(match.getTour().getStartDate()) && eventDate.before(match.getTour().getEndDate())){
            return true;
        }

        return false;
    }
}
