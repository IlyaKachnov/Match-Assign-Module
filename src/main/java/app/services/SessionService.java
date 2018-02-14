package app.services;

import app.models.SlotSignificationTime;

import java.util.List;

public interface SessionService {
    SlotSignificationTime findById(Long id);

    List<SlotSignificationTime> findAll();

    void save(SlotSignificationTime slotSignificationTime);

    void delete(Long id);
}
