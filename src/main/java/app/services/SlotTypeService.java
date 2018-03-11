package app.services;

import app.models.SlotType;

import java.util.List;

public interface SlotTypeService {

    SlotType findById(Long id);

    List<SlotType> findAll();

    void save(SlotType slotType);

    void delete(Long id);
}
