package app.repositories;

import app.models.SlotSignificationTime;
import app.models.SlotType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlotSignificationTimeRepository extends JpaRepository<SlotSignificationTime, Long> {
}
