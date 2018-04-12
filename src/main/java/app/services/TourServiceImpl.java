package app.services;

import app.models.Tour;
import app.repositories.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class TourServiceImpl implements TourService {
    @Autowired
    TourRepository tourRepository;

    @Override
    public Tour findById(Long id) {
        return tourRepository.findOne(id);
    }

    @Override
    public List<Tour> findAll() {
        return tourRepository.findAll();
    }

    @Override
    public void save(Tour tour) {
        tourRepository.save(tour);
    }

    @Override
    public void delete(Long id) {
        tourRepository.delete(id);
    }
}
