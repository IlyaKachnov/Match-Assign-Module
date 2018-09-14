package app.services;

import app.dto.MatchCalendarDTO;
import app.models.Match;
import app.repositories.MatchRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class MatchServiceImpl implements MatchService {


    private MatchRepository matchRepository;
    private Gson gson;

    @Autowired
    public MatchServiceImpl(MatchRepository matchRepository, Gson gson) {
        this.matchRepository = matchRepository;
        this.gson = gson;
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

    public String generateJSON() {

        List<Match> matches = matchRepository.findAll();
        if (matches.isEmpty()) {
            return "[]";
        }

        List<MatchCalendarDTO> matchCalendarDTOS = MatchCalendarDTO.createMatchCalendarList(matches);

        return gson.toJson(matchCalendarDTOS);


    }
}
