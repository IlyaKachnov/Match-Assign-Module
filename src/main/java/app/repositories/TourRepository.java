package app.repositories;

import app.models.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface TourRepository extends JpaRepository<Tour, Long> {
    Tour findByName(String name);
    @Query("select distinct t from Tour t inner join t.matches m")
    List<Tour> findWithMatches();
}
