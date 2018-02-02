package app.services;

import app.models.League;
import app.repositories.LeagueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class LeagueServiceImpl implements LeagueService {
    @Autowired
    LeagueRepository leagueRepository;

    @Override
    public List<League> findAll() {
        return this.leagueRepository.findAll();
    }

    @Override
    public void save(League league) {
        this.leagueRepository.save(league);
    }

    @Override
    public League findById(Long id) {
        return this.leagueRepository.findOne(id);
    }

    @Override
    public void delete(Long id) {
        this.leagueRepository.delete(id);
    }
}
