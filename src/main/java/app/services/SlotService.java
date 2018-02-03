package app.services;

import app.models.Slot;

import java.util.List;

public interface SlotService {

    Slot findById(Long id);

    List<Slot> findAll();

    void save(Slot slot);

    void delete(Long id);
}
