package app.services;

import app.models.MatchMessage;
import app.models.User;
import app.repositories.MatchMessageRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class MatchMessageServiceImpl implements MatchMessageService {

    private final MatchMessageRepository matchMessageRepository;

    public MatchMessageServiceImpl(MatchMessageRepository matchMessageRepository) {
        this.matchMessageRepository = matchMessageRepository;

    }

    @Override
    public MatchMessage findById(Long id) {
        return matchMessageRepository.findById(id).orElse(null);
    }

    @Override
    public List<MatchMessage> findAll() {
        return matchMessageRepository.findAll();
    }

    @Override
    public void save(MatchMessage matchMessage) {
        matchMessageRepository.save(matchMessage);
    }

    @Override
    public void delete(Long id) {
        matchMessageRepository.deleteById(id);
    }

    @Override
    public List<MatchMessage> getMessagesForHomeTeam(User user) {
        return matchMessageRepository.getMessagesForHomeTeam(user);
    }

    @Override
    public void store(MatchMessage matchMessage) {
        matchMessageRepository.save(matchMessage);
    }
}
