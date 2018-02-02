package app.services;

import app.models.Team;
import app.repositories.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class TeamServiceImpl implements TeamService{
    @Autowired
    TeamRepository teamRepository;

    @Override
    public List<Team> findAll() {
        return this.teamRepository.findAll();
    }

    @Override
    public void save(Team team) {
        this.teamRepository.save(team);
    }

    @Override
    public Team findById(Long id) {
        return this.teamRepository.findOne(id);
    }

    @Override
    public void delete(Long id) {
        this.teamRepository.delete(id);
    }
}
