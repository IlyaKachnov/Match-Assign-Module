package app.services;

import app.models.SlotType;

import java.util.Date;
import java.util.List;

public interface SlotTypeService {

    SlotType findById(Long id);

    SlotType findByTypeName(String name);

    List<SlotType> findAll();

    void save(SlotType slotType);

    void delete(Long id);

    boolean findExisted(String name, Date duration);
}
