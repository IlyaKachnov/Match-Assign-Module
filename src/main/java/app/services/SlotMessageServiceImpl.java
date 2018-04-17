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

    @Autowired
    SlotMessageRepository slotMessageRepository;

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
    }

    @Override
    public void delete(Long id) {
        slotMessageRepository.delete(id);
    }
}
