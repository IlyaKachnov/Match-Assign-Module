package app.services;

import app.dto.StadiumSlotsDTO;
import app.models.Stadium;
import app.models.User;

import java.util.List;

public interface StadiumService {

    Stadium findById(Long id);

    List<Stadium> findAll();

    void save(Stadium stadium);

    void delete(Long id);

    Stadium findByName(String name);

    List<Stadium> findAllWithSlots();

    List<StadiumSlotsDTO> findAllWithSlotsByUser(User user);

    List<Stadium> findAllWithSlotsByDate();
}
