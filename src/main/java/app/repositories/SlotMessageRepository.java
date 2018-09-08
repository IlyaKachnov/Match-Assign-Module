package app.repositories;

import app.models.MatchMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlotMessageRepository extends JpaRepository<MatchMessage, Long> {
}
