package app.repositories;

import app.models.SlotSignificationTime;
import app.models.SlotType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SlotSignificationTimeRepository extends JpaRepository<SlotSignificationTime, Long> {

    @Query("select distinct sst from SlotSignificationTime sst inner join sst.league l inner join l.teams t " +
            " where t.user.id = :userId and ((sst.startDate > current_date) or (sst.startDate = current_date " +
            "and sst.startTime > CURRENT_TIME)) ")
    List<SlotSignificationTime> getFutureSessions(@Param("userId") Long userId);

    @Query("select distinct sst from SlotSignificationTime sst inner join sst.league l inner join l.teams t " +
            " where (sst.startDate > current_date) or (sst.startDate = current_date and sst.startTime > CURRENT_TIME) ")
    List<SlotSignificationTime> getFutureSessions();

    @Query("select distinct sst from SlotSignificationTime sst inner join sst.league l inner join l.teams t " +
            " where t.user.id = :userId and ((sst.startDate < current_date and sst.endDate > current_date ) " +
            " or (sst.endDate = current_date and (sst.startTime < CURRENT_TIME and sst.endTime > CURRENT_TIME))" +
            " or (sst.startDate = current_date and (sst.startTime < CURRENT_TIME and sst.endTime > CURRENT_TIME)))")
    List<SlotSignificationTime> getActualSessions(@Param("userId") Long userId);

    @Query("select distinct sst from SlotSignificationTime sst inner join sst.league l inner join l.teams t " +
            " where (sst.startDate < current_date and sst.endDate > current_date) " +
            " or (sst.endDate = current_date and (sst.startTime < CURRENT_TIME and sst.endTime > CURRENT_TIME))" +
            " or (sst.startDate = current_date and (sst.startTime < CURRENT_TIME and sst.endTime > CURRENT_TIME))")
    List<SlotSignificationTime> getActualSessions();
}
