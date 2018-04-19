package app.services;

import app.models.Match;
import app.repositories.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class MatchServiceImpl implements MatchService {
    @Autowired
    MatchRepository matchRepository;

    @Override
    public List<Match> findAll() {
        return matchRepository.findAll();
    }

    @Override
    public Match findById(Long id) {
        return matchRepository.findOne(id);
    }

    @Override
    public void save(Match match) {
        matchRepository.save(match);
    }

    @Override
    public void delete(Long id) {
        matchRepository.delete(id);
    }

    public String generateJSON() {

        List<Match> matches = matchRepository.findAll();
        if (matches.isEmpty()) {
            return "[]";
        }
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
            json.append("\"Tour\": \"").append(match.getTour().getFullInfo()).append("\"},");
        });
        json.deleteCharAt(json.lastIndexOf(","));
        json.append("]");

        return json.toString();

    }
}
