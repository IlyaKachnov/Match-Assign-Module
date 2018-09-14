package app.services;

import app.models.SlotType;
import app.repositories.SlotTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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
        return slotTypeRepository.findById(id).orElse(null);
    }

    @Override
    public void save(SlotType slotType) {
        slotTypeRepository.save(slotType);
    }

    @Override
    public SlotType findByTypeName(String name) {
        return slotTypeRepository.findByTypeName(name);
    }

    @Override
    public void delete(Long id) {
        slotTypeRepository.deleteById(id);
    }

    @Override
    public boolean findExisted(String name, Date duration) {
        return slotTypeRepository.findAll().stream().anyMatch(sT -> sT.getTypeName().equals(name) && sT.getDuration().equals(duration));
    }
}
