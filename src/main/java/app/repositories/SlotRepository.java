package app.repositories;

import app.models.Slot;
import app.models.Stadium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;





public interface SlotRepository extends JpaRepository<Slot, Long> {

    @Query("select sl from Slot sl where sl.stadium = :stadium and sl.eventDate between :startDate and :endDate")
    List<Slot> findAllSlotsByStadiumAndDate(@Param("stadium") Stadium stadium, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
