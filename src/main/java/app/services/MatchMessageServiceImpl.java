package app.services;

import app.models.MatchMessage;
import app.repositories.SlotMessageRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class MatchMessageServiceImpl implements MatchMessageService {

    private final SlotMessageRepository slotMessageRepository;
    private final EmailServiceImpl emailService;

    public MatchMessageServiceImpl(SlotMessageRepository slotMessageRepository, EmailServiceImpl emailService) {
        this.slotMessageRepository = slotMessageRepository;
        this.emailService = emailService;
    }

    @Override
    public MatchMessage findById(Long id) {
        return slotMessageRepository.findById(id).orElse(null);
    }

    @Override
    public List<MatchMessage> findAll() {
        return slotMessageRepository.findAll();
    }

    @Override
    public void save(MatchMessage matchMessage) {
        boolean status = matchMessage.getConsidered();
        slotMessageRepository.save(matchMessage);
        String email;

        if(status) {
           email = matchMessage.getMatch().getGuestTeam().getUser().getEmail();
        }

        else {
            email = matchMessage.getMatch().getHomeTeam().getUser().getEmail();
        }

        emailService.sendNotification(email, matchMessage);
    }

    @Override
    public void delete(Long id) {
        slotMessageRepository.deleteById(id);
    }

    @Override
    public void store(MatchMessage matchMessage) {
        slotMessageRepository.save(matchMessage);
    }
}
