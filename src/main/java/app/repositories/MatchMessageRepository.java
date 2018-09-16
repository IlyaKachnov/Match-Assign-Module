package app.repositories;

import app.models.MatchMessage;
import app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MatchMessageRepository extends JpaRepository<MatchMessage, Long> {

    @Query(value = "SELECT message from MatchMessage message inner join message.match match inner join match.homeTeam homeTeam where homeTeam.user = ?1")
    List<MatchMessage> getMessagesForHomeTeam(User user);

}
