package app.repositories;

import app.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {

    Team findByName(String name);
    List<Team> findAllByOrderByNameAsc();
}
