package app.dto;

import app.models.Tour;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TourFilterDTO {
    private String title;

    public TourFilterDTO(Tour tour) {
        this.title = tour.getName();
    }

    public static Set<TourFilterDTO> createTourList(List<Tour> tours){
        return tours.stream().map(tour -> new TourFilterDTO(tour)).collect(Collectors.toSet());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
