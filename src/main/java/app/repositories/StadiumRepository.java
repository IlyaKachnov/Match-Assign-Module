package app.repositories;

import app.models.Stadium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StadiumRepository extends JpaRepository<Stadium, Long> {
    Stadium findByName(String name);
    @Query("select distinct s from Stadium s inner join s.slots sl ")
    List<Stadium> findAllWithSlots();

}
