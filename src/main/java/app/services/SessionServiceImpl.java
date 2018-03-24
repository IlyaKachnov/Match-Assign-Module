package app.services;

import app.models.SlotSignificationTime;
import app.repositories.SlotSignificationTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public SlotSignificationTime getWithLeague(SlotSignificationTime significationTime) {
        Optional<SlotSignificationTime> optional = repository.findAll().stream().filter(sT -> sT.getLeague()
                .equals(significationTime.getLeague()) && !sT.getLeague()
                .equals(significationTime.getLeague()))
                .findAny();

        optional.get().setStartDate(significationTime.getStartDate());
        optional.get().setEndDate(significationTime.getEndDate());
        optional.get().setStartTime(significationTime.getStartTime());
        optional.get().setEndTime(significationTime.getEndTime());

        return optional.get();

    }


}
