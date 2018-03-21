package app.repositories;

import app.models.Stadium;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StadiumRepository extends JpaRepository<Stadium, Long> {
    Stadium findByName(String name);

}
