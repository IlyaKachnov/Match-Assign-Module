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

    @Override
    public Tour findByName(String name) {
        return tourRepository.findByName(name);
    }

    @Override
    public String generateJSON() {
        StringBuilder json = new StringBuilder("{");
        List<Tour> tours = tourRepository.findAll();
        tours.forEach(tour -> {
            json.append("\"").append(tour.getFullInfo()).append("\"").append(":");
            json.append("{\"title\": \"").append(tour.getFullInfo()).append("\"},");
        });
        json.deleteCharAt(json.lastIndexOf(","));
        json.append("}");

        return json.toString();
    }
}
