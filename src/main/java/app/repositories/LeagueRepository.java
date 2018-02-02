package app.repositories;

import app.models.League;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeagueRepository extends JpaRepository<League,Long> {
}
