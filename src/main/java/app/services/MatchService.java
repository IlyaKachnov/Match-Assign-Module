package app.services;

import app.models.Match;

import java.util.List;

public interface MatchService {
    Match findById(Long id);

    List<Match> findAll();

    void save(Match match);

    void delete(Long id);

}
