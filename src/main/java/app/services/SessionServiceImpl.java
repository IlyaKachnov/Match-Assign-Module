package app.services;

import app.models.SlotSignificationTime;
import app.repositories.SlotSignificationTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class SessionServiceImpl implements SessionService {

    @Autowired
    SlotSignificationTimeRepository repository;

    @Override
    public SlotSignificationTime findById(Long id) {
        return this.repository.findOne(id);
    }

    @Override
    public List<SlotSignificationTime> findAll() {
        return this.repository.findAll();
    }

    @Override
    public void save(SlotSignificationTime slotSignificationTime) {
        this.repository.save(slotSignificationTime);
    }

    @Override
    public void delete(Long id) {
        this.repository.delete(id);
    }
}
