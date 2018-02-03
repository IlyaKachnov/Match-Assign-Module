package app.services;

import app.models.Slot;
import app.repositories.SlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class SlotServiceImpl implements SlotService{

    @Autowired
    SlotRepository slotRepository;

    @Override
    public Slot findById(Long id) {
        return this.slotRepository.findOne(id);
    }

    @Override
    public List<Slot> findAll() {
        return this.slotRepository.findAll();
    }

    @Override
    public void save(Slot slot) {
        this.slotRepository.save(slot);
    }

    @Override
    public void delete(Long id) {
        this.slotRepository.delete(id);
    }
}
