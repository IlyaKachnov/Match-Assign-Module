package app.repositories;

import app.models.League;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LeagueRepository extends JpaRepository<League, Long> {
    League findByName(String name);
    @Query("select l from League l inner join l.tours t inner join t.matches m")
    List<League> findWithMatches();
}
