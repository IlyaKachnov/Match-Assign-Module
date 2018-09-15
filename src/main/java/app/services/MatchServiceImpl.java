package app.services;

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
    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private UserRepository userRepository;

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

            json.append("{\"Home\": \"").append(match.getHomeTeam().getName()).append("\",");
            json.append("\"Guest\": \"").append(match.getGuestTeam().getName()).append("\",");
            json.append("\"MatchDate\": \"").append(match.getFormattedDate()).append("\",");
            json.append("\"Delayed\": \"").append(isDelayed).append("\",");
            json.append("\"Stadium\": \"").append(stadium).append("\",");
            json.append("\"League\": \"").append(match.getTour().getLeague().getName()).append("\",");
            json.append("\"Message\": \"").append(getMessage(match, userTeams, isAdmin)).append("\",");
            json.append("\"Tour\": \"").append(match.getTour().getFullInfo()).append("\"},");
        });
        json.deleteCharAt(json.lastIndexOf(","));
        json.append("]");

        return json.toString();

    }

    private String getMessage(Match match, List<Team> userTeams, boolean isAdmin) {
        if ((userTeams.contains(match.getHomeTeam()) || isAdmin) && match.getMatchMessage() != null) {
            return getMessageForHomeTeam(match);
        } else if (userTeams.contains(match.getGuestTeam())) {
            return getMessageForGuestTeam(match);
        }
        return " - - -";
    }

    private String getMessageForHomeTeam(Match match) {
        MatchMessage matchMessage = match.getMatchMessage();

        String message = "<button type='button' class='m-portlet__nav-link btn m-btn m-btn--hover-accent m-btn--icon m-btn--icon-only m-btn--pill' " +
                "data-toggle='m-popover' data-trigger='focus' title='' data-html='true' data-content='"
                + matchMessage.getMessage() + "' data-original-title='" +
                (matchMessage.getConsidered() ? "Запрос рассмотрен" : "Новое сообщение") + "'" +
                "><i class='la la-envelope'></i></button>";

        return message;
    }

    private String getMessageForGuestTeam(Match match) {
        if (match.getMatchMessage() != null) {
            return getMessageForHomeTeam(match);
        }

        String message = " <a class='add-msg m-portlet__nav-link btn m-btn m-btn--hover-accent m-btn--icon m-btn--icon-only m-btn--pill' " +
                "href='#m_modal_4' data-action='/save-message/" +
                match.getId() + "'" + " data-toggle='modal' data-target='#m_modal_4'><i class='la la-envelope'></i></a>";

        return message;
    }
}
