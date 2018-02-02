package app.services;

import app.models.Team;

import java.util.List;

public interface TeamService {
    List<Team> findAll();

    void save(Team team);

    void delete(Long id);

    Team findById(Long id);
}
