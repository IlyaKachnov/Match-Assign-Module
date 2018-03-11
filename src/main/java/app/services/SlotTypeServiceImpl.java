package app.services;

import app.models.SlotType;
import app.repositories.SlotTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class SlotTypeServiceImpl implements SlotTypeService {

    @Autowired
    SlotTypeRepository slotTypeRepository;
    @Override
    public List<SlotType> findAll() {
        return slotTypeRepository.findAll();
    }

    @Override
    public SlotType findById(Long id) {
        return slotTypeRepository.findOne(id);
    }

    @Override
    public void save(SlotType slotType) {
        slotTypeRepository.save(slotType);
    }

    @Override
    public void delete(Long id) {
        slotTypeRepository.delete(id);
    }
}
