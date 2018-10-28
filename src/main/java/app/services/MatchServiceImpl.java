package app.services;

import app.dto.MatchDTO;
import com.google.gson.Gson;
import app.models.*;
import app.repositories.MatchRepository;
import app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
@Primary
public class MatchServiceImpl implements MatchService {


    private MatchRepository matchRepository;
    private Gson gson;
    private UserRepository userRepository;

    @Autowired
    public MatchServiceImpl(MatchRepository matchRepository, Gson gson, UserRepository userRepository) {
        this.matchRepository = matchRepository;
        this.gson = gson;
        this.userRepository = userRepository;
    }

    @Override
    public List<Match> findAll() {
        return matchRepository.findAll();
    }

    @Override
    public List<MatchDTO> getMatchDTOList(){
        List<Match> matches = matchRepository.findAll();
        List<MatchDTO> matchDTOList = new ArrayList<>();
        if (!matches.isEmpty()) {
            matches.forEach(match -> {
                MatchDTO matchDTO = new MatchDTO();
                Tour tour = match.getTour();
                String isDelayed = match.getDelayed() ? "Да" : "Нет";
                Slot slot = match.getSlot();
                String stadium = (slot != null) ? slot.getStadium().getName() : "Не назначен";
                boolean status = (slot != null);
                matchDTO.setId(match.getId());
                matchDTO.setHome(match.getHomeTeam().getName());
                matchDTO.setGuest(match.getGuestTeam().getName());
                matchDTO.setTour(tour.getFullInfo());
                matchDTO.setLeague(tour.getLeague().getName());
                matchDTO.setDelayed(isDelayed);
                matchDTO.setStadium(stadium);
                matchDTO.setMatchDate(getFormattedMatchDate(match));
                matchDTO.setStatus(status);

                matchDTOList.add(matchDTO);
            });
        }
        return matchDTOList;
    }

    @Override
    public Match findById(Long id) {
        return matchRepository.findById(id).orElse(null);
    }

    @Override
    public void save(Match match) {
        matchRepository.save(match);
    }

    @Override
    public void delete(Long id) {
        matchRepository.deleteById(id);
    }

    @Override
    public String generateJSON(String userEmail) {
        List<MatchDTO> matchDTOList = getMatchDTOList(userEmail);
        return gson.toJson(matchDTOList);

    }

    @Override
    public List<MatchDTO> getMatchDTOList(String userEmail) {
        User currUser = userRepository.findByEmail(userEmail);
        boolean isAdmin = currUser.getRole().equals(Role.adminRole);
        List<Match> matches = matchRepository.findAll();
        List<MatchDTO> matchDTOList = new ArrayList<>();
        List<Team> userTeams = currUser.getTeamList();
        if (!matches.isEmpty()) {
            matches.forEach(match -> {
                MatchDTO matchDTO = new MatchDTO();
                Tour tour = match.getTour();
                String isDelayed = match.getDelayed() ? "Да" : "Нет";
                String stadium = (match.getSlot() != null) ? match.getSlot().getStadium().getName() : "Не назначен";

                matchDTO.setId(match.getId());
                matchDTO.setHome(match.getHomeTeam().getName());
                matchDTO.setGuest(match.getGuestTeam().getName());
                matchDTO.setTour(tour.getFullInfo());
                matchDTO.setLeague(tour.getLeague().getName());
                matchDTO.setDelayed(isDelayed);
                matchDTO.setStadium(stadium);
                matchDTO.setMessage(getMessage(match, userTeams, isAdmin));
                matchDTO.setMatchDate(getFormattedMatchDate(match));

                matchDTOList.add(matchDTO);
            });
        }
        return matchDTOList;
    }

    private String getMessage(Match match, List<Team> userTeams, boolean isAdmin) {
        if (match.getMatchMessage() != null) {
            return getMatchMessage(match);
        } else if (userTeams.contains(match.getGuestTeam()) || userTeams.contains(match.getHomeTeam()) || isAdmin) {
            return getMessageForm(match);
        }
        return "";
    }

    private String getMatchMessage(Match match) {
        MatchMessage matchMessage = match.getMatchMessage();

        String message = "<button type='button' class='m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill' " +
                "data-toggle='m-popover' data-trigger='focus' title='' data-html='true' data-content='"
                + matchMessage.getMessage().replace("\n", " ").replace("\r", "") +
                "' data-original-title='" +
                (matchMessage.getConsidered() ? "Запрос рассмотрен" : "Новое сообщение") + "'" +
                "><i class='la la-envelope'></i></button>";

        return message;
    }

    private String getMessageForm(Match match) {
        String message = " <a class='add-msg m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill' " +
                "href='#m_modal_4' data-action='/save-message/" +
                match.getId() + "'" + " data-toggle='modal' data-target='#m_modal_4'><i class='la la-plus-circle'></i></a>";

        return message;
    }

    private String getFormattedMatchDate(Match match) {

        if (match.getMatchDate() == null) {
            return "Не назначен";
        }
        String matchDate = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        matchDate += dateFormat.format(match.getMatchDate()) + " / " +
                timeFormat.format(match.getSlot().getStartTime());
        return matchDate;
    }

}
