package app.services;

import app.dto.MatchCalendarDTO;
import app.models.Match;
import app.repositories.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<MatchCalendarDTO> generateJSON() {

        List<Match> matches = matchRepository.findAll();
//        List<MatchCalendarDTO> matchCalendarDTOS = new ArrayList<>();
        if (matches.isEmpty()) {
            return new ArrayList<>();
        }
//        StringBuilder json = new StringBuilder("[");
//
//        matches.forEach(match -> {
//            String isDelayed = match.getDelayed() ? "Да" : "Нет";
//            String stadium = (match.getSlot() != null) ? match.getSlot().getStadium().getName() : "Не назначен";
//
//            json.append("{\"home\": \"").append(match.getHomeTeam().getName()).append("\",");
//            json.append("\"guest\": \"").append(match.getGuestTeam().getName()).append("\",");
//            json.append("\"matchDate\": \"").append(match.getFormattedDate()).append("\",");
//            json.append("\"delayed\": \"").append(isDelayed).append("\",");
//            json.append("\"stadium\": \"").append(stadium).append("\",");
//            json.append("\"league\": \"").append(match.getTour().getLeague().getName()).append("\",");
//            json.append("\"tour\": \"").append(match.getTour().getFullInfo()).append("\"},");
//        });
//        json.deleteCharAt(json.lastIndexOf(","));
//        json.append("]");
//
//        System.out.println(json);
        List<MatchCalendarDTO> matchCalendarDTOS = new ArrayList<>();
        matchCalendarDTOS = MatchCalendarDTO.createMatchCalendarList(matches);
        System.out.println(matchCalendarDTOS);
        return matchCalendarDTOS;

//        return json.toString();

    }
}
