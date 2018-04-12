package app.services;

import app.models.Tour;

import java.util.List;

public interface TourService {
    List<Tour> findAll();

    void save(Tour tour);

    void delete(Long id);

    Tour findById(Long id);

}
