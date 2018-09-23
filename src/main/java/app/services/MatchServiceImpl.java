package app.services;

import com.google.gson.Gson;
import app.models.*;
import app.repositories.MatchRepository;
import app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

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
        User currUser = userRepository.findByEmail(userEmail);
        boolean isAdmin = currUser.getRole().equals(Role.adminRole);

        List<Match> matches = matchRepository.findAll();
        if (matches.isEmpty()) {
            return "[]";
        }
        List<Team> userTeams = currUser.getTeamList();

        StringBuilder json = new StringBuilder("[");

        matches.forEach(match -> {
            String isDelayed = match.getDelayed() ? "Да" : "Нет";
            String stadium = (match.getSlot() != null) ? match.getSlot().getStadium().getName() : "Не назначен";

            json.append("{\"home\": \"").append(match.getHomeTeam().getName()).append("\",");
            json.append("\"guest\": \"").append(match.getGuestTeam().getName()).append("\",");
            json.append("\"matchDate\": \"").append(match.getFormattedDate()).append("\",");
            json.append("\"delayed\": \"").append(isDelayed).append("\",");
            json.append("\"stadium\": \"").append(stadium).append("\",");
            json.append("\"league\": \"").append(match.getTour().getLeague().getName()).append("\",");
            json.append("\"message\": \"").append(getMessage(match, userTeams, isAdmin)).append("\",");
            json.append("\"tour\": \"").append(match.getTour().getFullInfo()).append("\"},");
        });
        json.deleteCharAt(json.lastIndexOf(","));
        json.append("]");

        return json.toString();

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
}
