package app.services;

import app.dto.MatchDTO;
import app.models.Match;

import java.util.List;

public interface MatchService {
    Match findById(Long id);

    List<Match> findAll();

    void save(Match match);

    void delete(Long id);

    String generateJSON(String userEmail);

    List<MatchDTO> getMatchDTOList();

    List<MatchDTO> getMatchDTOList(String userEmail);
}
