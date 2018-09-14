package app.services;

import app.dto.TourFilterDTO;
import app.models.Tour;
import app.repositories.TourRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Primary
public class TourServiceImpl implements TourService {


    private TourRepository tourRepository;
    private Gson gson;

    @Autowired
    public TourServiceImpl(TourRepository tourRepository, Gson gson) {

        this.tourRepository = tourRepository;
        this.gson = gson;
    }

    @Override
    public Tour findById(Long id) {
        return tourRepository.findById(id).orElse(null);
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
        tourRepository.deleteById(id);
    }

    @Override
    public List<Tour> findWithMatches() {
        return tourRepository.findWithMatches();
    }

    @Override
    public Tour findByName(String name) {
        return tourRepository.findByName(name);
    }

    @Override
    public String generateTourFilterJSON() {
        List<Tour> tours = tourRepository.findWithMatches();
        if (tours == null && tours.isEmpty()) {
            return "[]";
        }

        Set<TourFilterDTO> tourFilterDTOS = TourFilterDTO.createTourList(tours);
        return gson.toJson(tourFilterDTOS);
    }
}
