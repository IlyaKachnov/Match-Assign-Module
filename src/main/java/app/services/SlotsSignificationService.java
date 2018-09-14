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
    private final MatchMessageService messageService;
    private final StadiumService stadiumService;
    private final MatchService matchService;

    @Autowired
    public SlotsSignificationService(StadiumRepository stadiumRepository,
                                     MatchRepository matchRepository,
                                     SlotRepository slotRepository,
                                     SlotSignificationTimeRepository slotSignificationTimeRepository,
                                     UserRepository userRepository,
                                     MatchMessageService messageService,
                                     StadiumService stadiumService,
                                     MatchService matchService) {
        this.stadiumRepository = stadiumRepository;
        this.matchRepository = matchRepository;
        this.slotRepository = slotRepository;
        this.slotSignificationTimeRepository = slotSignificationTimeRepository;
        this.userRepository = userRepository;
        this.messageService = messageService;
        this.stadiumService = stadiumService;
        this.matchService = matchService;
    }

    public String generateSlotsJSON(Long id, String userEmail) {
        List<Slot> slots;
        Stadium stadium = stadiumService.findById(id);
        if (stadium == null) {
            return null;
        } else if (stadium.getSlots().isEmpty()) {
            return null;
        }
        slots = stadium.getSlots();
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
        Optional<Slot> currSlotOptional = slotRepository.findById(slotId);
        if (!currSlotOptional.isPresent()) {
            return null;
        }
        Slot currSlot = currSlotOptional.get();

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
        Match match = matchService.findById(matchId);
        MatchMessage message = match.getMatchMessage();
        if (match != null && match.getSlot() != null) {
            return;
        }
        if (match != null && match.getHomeTeam() != null) {
            Optional<SlotSignificationTime> slotSignificationTimeOptional = slotSignificationTimeRepository.findAll()
                    .stream().filter(slotSignificationTime -> slotSignificationTime.getLeague().equals(match.getHomeTeam().getLeague()))
                    .findAny();
            if (currUser.getRole().equals(Role.adminRole) ||
                    (slotSignificationTimeOptional.isPresent() && checkDateTime(slotSignificationTimeOptional.get()) &&
                            match.getHomeTeam().getUser().equals(currUser))
                    ) {

                Optional<Slot> currSlotOptional = slotRepository.findById(slotId);
                if (!currSlotOptional.isPresent()) {
                    return;
                }
                Slot currSlot = currSlotOptional.get();
                if (currSlot.getMatch() == null && currSlot.getSlotType().getSignifiable()) {
                    currSlot.setMatch(match);
                    match.setMatchDate(currSlot.getEventDate());
                }
                if (message != null && message.getConsidered()) {
                    messageService.save(message);

                }
                slotRepository.save(currSlot);
                matchRepository.save(match);
            }
        }
    }

    public void rejectSlot(Long slotId, String userEmail) {
        Optional<Slot> currSlotOptional = slotRepository.findById(slotId);
        if (!currSlotOptional.isPresent()) {
            return;
        }
        Slot currSlot = currSlotOptional.get();
        Match currMatch = currSlot.getMatch();
        MatchMessage message = currMatch.getMatchMessage();

        User currUser = userRepository.findByEmail(userEmail);
        if (currUser.getRole().equals(Role.adminRole)) {
            if (message != null) {
                message.setConsidered(true);
                messageService.store(message);
            }
            currMatch.setMatchDate(null);
            currSlot.setMatch(null);
            matchRepository.save(currMatch);
            slotRepository.save(currSlot);
        } else if (currMatch != null && currMatch.getHomeTeam() != null) {
            Optional<SlotSignificationTime> slotSignificationTimeOptional = slotSignificationTimeRepository.findAll()
                    .stream()
                    .filter(slotSignificationTime -> slotSignificationTime.getLeague().equals(currMatch.getHomeTeam().getLeague()))
                    .findAny();
            if ((slotSignificationTimeOptional.isPresent() && checkDateTime(slotSignificationTimeOptional.get()))
                    || currMatch.getHomeTeam().getUser().equals(currUser)) {
                if (message != null) {
                    message.setConsidered(true);
                    messageService.store(message);
                }
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
            slotsJSON.append("\"message\": \"").append(getMessage(slot)).append("\",");
            slotsJSON.append("\"start\": \"").append(slot.getEventDate())
                    .append(" ").append(slot.getStartTime()).append("\",");
            slotsJSON.append("\"end\": \"").append(slot.getEventDate())
                    .append(" ").append(slot.getEndTime()).append("\",");
            if (slot.getMatch() != null) {
                if ((slot.getMatch().getHomeTeam().getUser() != null &&
                        slot.getMatch().getHomeTeam().getUser().getEmail().equals(userEmail) &&
                        leagues.contains(slot.getMatch().getHomeTeam().getLeague()))
                        || currUser.getRole().equals(Role.adminRole)
                        || slot.getMatch().getHomeTeam().getUser().equals(currUser)) {
                    slotsJSON.append(addMatchTeams(slot.getMatch()))
                            .append(" <a class='m_sweetalert cancel' href='/stadium/").append(id).append("/reject/")
                            .append(slot.getId()).append("'>Отменить слот</a>")
                            .append("\"},");
                } else if (slot.getMatch().getGuestTeam().getUser() != null &&
                        slot.getMatch().getGuestTeam().getUser().getEmail().equals(userEmail)) {
                    slotsJSON.append(addMatchTeams(slot.getMatch()))
                            .append(showMessageLink(slot.getMatch()));
                } else {
                    slotsJSON.append(addMatchTeams(slot.getMatch()))
                            .append("\"},");
                }
            } else if (slot.getSlotType().getSignifiable() &&
                    (!leagues.isEmpty() || currUser.getRole().equals(Role.adminRole))
                    || !getMatchesWithMessages(currUser).isEmpty()) {
                slotsJSON.append("\"description\": \" <a class='add-new' href='/stadium/")
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

    private boolean consistsWithTour(Match match, Date eventDate) {
        if (match.getDelayed()) {
            return true;
        }

        if (eventDate.after(match.getTour().getStartDate()) && eventDate.before(match.getTour().getEndDate())) {
            return true;
        }

        return false;
    }

    private String getMessage(Slot slot) {

        if (slot.getMatch() == null) {
            return "";
        }
        MatchMessage message = slot.getMatch().getMatchMessage();
        return (message != null)
                ? "<span href='#' tabindex='0' role='button' " +
                "class='m-badge " + (message.getConsidered() ? "m-badge--warning" : "m-badge--danger") +
                "' data-toggle='m-popover' " +
                "data-trigger='popover' data-content='"
                + message.getMessage() + "' data-original-title='" +
                (message.getConsidered() ? "Запрос рассмотрен" : "Новое сообщение") + "'"
                + "style='outline: none; margin-left: 2px;'>!</span>"
                : "";
    }

    private String showMessageLink(Match match) {
        if (match.getMatchMessage() != null) {
            return " <a href='#' class='delete-msg' data-href='/message/" +
                    match.getMatchMessage().getMatch().getId()
                    + "/delete'" + ">Удалить сообщение</a>" +
                    "\"},";
        }

        return " <a class='add-msg' href='#m_modal_4' data-action='/save-message/" +
                match.getId() + "'" + " data-toggle='modal' data-target='#m_modal_4'>Предложить другое время</a>" +
                "\"},";
    }

    private String addMatchTeams(Match match) {
        return "\"description\": \"" + match.getHomeAndGuest();

    }

    public List<Match> getMatchesWithMessages(User user) {
        List<Match> matchList = new ArrayList<>();
        List<Team> teamList = user.getTeamList();

        if (teamList.isEmpty()) {
            return Collections.emptyList();
        }

        teamList.forEach(team -> {
            if (team.getMatchesAsHome().isEmpty()) {
                return;
            }
            team.getMatchesAsHome().forEach(match -> {
                if (match.getMatchMessage() != null && match.getMatchMessage().getConsidered() && match.getSlot() == null) {
                    matchList.add(match);
                }
            });
        });

        return matchList;
    }
}
