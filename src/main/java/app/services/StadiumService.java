package app.services;

import app.models.Stadium;

import java.util.List;

public interface StadiumService {

    Stadium findById(Long id);

    List<Stadium> findAll();

    void save(Stadium stadium);

    void delete(Long id);

    Stadium findByName(String name);
}
