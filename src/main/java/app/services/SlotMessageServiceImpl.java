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
        slotMessageRepository.save(slotMessage);
        emailService.sendNotification(slotMessage.getMatch().getHomeTeam().getUser().getEmail(), slotMessage);

    }

    @Override
    public void delete(Long id) {
        slotMessageRepository.delete(id);
    }
}
