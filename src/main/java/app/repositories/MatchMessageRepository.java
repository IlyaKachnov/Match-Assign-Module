package app.repositories;

import app.models.MatchMessage;
import app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MatchMessageRepository extends JpaRepository<MatchMessage, Long> {

    @Query(value = "SELECT message from MatchMessage message inner join message.match match inner join match.homeTeam " +
            " homeTeam " +
            "inner join match.guestTeam guestTeam " +
            "where ((homeTeam.user = ?1 or guestTeam.user = ?1) and message.isRead = false ) " +
            "order by message.localDateTime desc ")
    List<MatchMessage> getMessagesForUser(User user);

}
