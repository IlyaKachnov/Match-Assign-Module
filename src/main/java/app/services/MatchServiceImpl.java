package app.services;

import app.models.Match;
import app.repositories.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class MatchServiceImpl implements MatchService{
    @Autowired
    MatchRepository matchRepository;

    @Override
    public List<Match> findAll() {
        return this.matchRepository.findAll();
    }

    @Override
    public Match findById(Long id) {
        return this.matchRepository.findOne(id);
    }

    @Override
    public void save(Match match) {
        this.matchRepository.save(match);
    }

    @Override
    public void delete(Long id) {
        this.matchRepository.delete(id);
    }
}
