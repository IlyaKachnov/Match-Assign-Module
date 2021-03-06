package app.services;

import app.models.League;

import java.util.List;

public interface LeagueService {
    List<League> findAll();

    void save(League league);

    void delete(Long id);

    League findById(Long id);

    List<League> findWithoutSession();

    League findByName(String name);

    String generateLeagueFilterJSON();

    String getTeamsJSON(Long id);

    List<League> findWithMatches();

}
