package app.services;

import app.models.SlotMessage;

import java.util.List;

public interface SlotMessageService {

    SlotMessage findById(Long id);

    List<SlotMessage> findAll();

    void save(SlotMessage slotMessage);

    void delete(Long id);
}
