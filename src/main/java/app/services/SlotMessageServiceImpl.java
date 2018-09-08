package app.services;

import app.models.MatchMessage;
import app.repositories.SlotMessageRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class SlotMessageServiceImpl implements SlotMessageService{

    private final SlotMessageRepository slotMessageRepository;
    private final EmailServiceImpl emailService;

    public SlotMessageServiceImpl(SlotMessageRepository slotMessageRepository, EmailServiceImpl emailService) {
        this.slotMessageRepository = slotMessageRepository;
        this.emailService = emailService;
    }

    @Override
    public MatchMessage findById(Long id) {
        return slotMessageRepository.findOne(id);
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
        slotMessageRepository.delete(id);
    }

    @Override
    public void store(MatchMessage matchMessage) {
        slotMessageRepository.save(matchMessage);
    }
}
