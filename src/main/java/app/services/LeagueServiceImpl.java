package app.services;

import app.models.League;
import app.models.Team;
import app.repositories.LeagueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        return this.leagueRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        this.leagueRepository.deleteById(id);
    }

    @Override
    public List<League> findWithoutSession() {
        return leagueRepository.findAll().stream()
                .filter(l -> l.getSlotSignificationTime() == null)
                .collect(Collectors.toList());
    }

    @Override
    public League findByName(String name) {
        return leagueRepository.findByName(name);
    }

    @Override
    public List<League> findWithMatches() {
        return leagueRepository.findWithMatches();
    }

    @Override
    public String generateJSON() {
        StringBuilder json = new StringBuilder("{");
//        List<League> leagues = leagueRepository.findAll();
        List<League> leagues = leagueRepository.findWithMatches();
        if (leagues.isEmpty()) {
            return "[]";
        }
        leagues.forEach(l -> {
            json.append("\"").append(l.getName()).append("\"").append(":");
            json.append("{\"title\": \"").append(l.getName()).append("\"},");
        });
        json.deleteCharAt(json.lastIndexOf(","));
        json.append("}");

        return json.toString();
    }

    @Override
    public String getTeamsJSON(Long id) {
        List<Team> teamList = findById(id).getTeams();
        StringBuilder json = new StringBuilder("[");
        teamList.forEach(team -> {
            json.append("{\"id\": \"").append(team.getId()).append("\",");
            json.append("\"name\": \"").append(team.getName()).append("\"},");
        });
        json.deleteCharAt(json.lastIndexOf(","));
        json.append("]");

        return json.toString();
    }
}
