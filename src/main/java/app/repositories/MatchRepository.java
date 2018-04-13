package app.repositories;

import app.models.Match;
import app.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findByHomeTeam(Team homeTeam);

}
