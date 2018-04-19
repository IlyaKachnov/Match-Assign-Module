package app.services;

import app.models.SlotMessage;
import app.repositories.SlotMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public SlotMessage findById(Long id) {
        return slotMessageRepository.findOne(id);
    }

    @Override
    public List<SlotMessage> findAll() {
        return slotMessageRepository.findAll();
    }

    @Override
    public void save(SlotMessage slotMessage) {
        boolean status = slotMessage.getConsidered();
        slotMessageRepository.save(slotMessage);
        String email;

        if(status) {
           email = slotMessage.getMatch().getGuestTeam().getUser().getEmail();
        }

        else {
            email = slotMessage.getMatch().getHomeTeam().getUser().getEmail();
        }

        emailService.sendNotification(email, slotMessage);
    }

    @Override
    public void delete(Long id) {
        slotMessageRepository.delete(id);
    }

    @Override
    public void store(SlotMessage slotMessage) {
        slotMessageRepository.save(slotMessage);
    }
}
