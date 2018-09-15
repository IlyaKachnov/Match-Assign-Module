package app.services;

import app.models.Tour;
import app.repositories.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Primary
public class TourServiceImpl implements TourService {


    private final TourRepository tourRepository;

    @Autowired
    public TourServiceImpl(TourRepository tourRepository) {
        this.tourRepository = tourRepository;
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
    public Tour findByName(String name) {
        return tourRepository.findByName(name);
    }

    @Override
    public Set<String> getToursInfo() {
        return tourRepository.findAll().stream()
                .filter(t-> !(t.getMatches().isEmpty()))
                .map(Tour::getFullInfo)
                .collect(Collectors.toSet());
    }

    @Override
    public String generateJSON() {
        StringBuilder json = new StringBuilder("{");
        Set<String> toursInfo = getToursInfo();

        if (toursInfo.isEmpty()) {
            return "[]";
        }
        toursInfo.forEach(s -> {
            json.append("\"").append(s).append("\"").append(":");
            json.append("{\"title\": \"").append(s).append("\"},");
        });
        json.deleteCharAt(json.lastIndexOf(","));
        json.append("}");

        return json.toString();
    }
}
