package app.services;

import app.models.Team;
import app.repositories.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Primary
public class TeamServiceImpl implements TeamService {
    @Autowired
    TeamRepository teamRepository;

    @Override
    public List<Team> findAll() {
        return teamRepository.findAll();
    }

    @Override
    public void save(Team team) {
        teamRepository.save(team);
    }

    @Override
    public Team findById(Long id) {
        return teamRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        this.teamRepository.deleteById(id);
    }

    public List<Team> findWithoutUser() {
        return teamRepository.findAll().stream()
                .filter(t -> t.getUser() == null)
                .collect(Collectors.toList());
    }

    @Override
    public Team findByName(String name) {
        return teamRepository.findByName(name);
    }

    @Override
    public List<Team> findAllOrderByName() {
        return teamRepository.findAllByOrderByNameAsc();
    }
}
