package app.services;

import app.models.Tour;

import java.util.List;
import java.util.Set;

public interface TourService {
    List<Tour> findAll();

    void save(Tour tour);

    void delete(Long id);

    Tour findById(Long id);

    Tour findByName(String name);

    String generateJSON();

    Set<String> getToursInfo();
}
