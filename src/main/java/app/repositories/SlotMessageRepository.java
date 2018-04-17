package app.repositories;

import app.models.SlotMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlotMessageRepository extends JpaRepository<SlotMessage, Long> {
}
