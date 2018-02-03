package app.services;

import app.models.Stadium;
import app.repositories.StadiumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class StadiumServiceImpl implements StadiumService {

    @Autowired
    StadiumRepository stadiumRepository;

    @Override
    public Stadium findById(Long id) {
        return this.stadiumRepository.findOne(id);
    }

    @Override
    public List<Stadium> findAll() {
        return this.stadiumRepository.findAll();
    }

    @Override
    public void save(Stadium stadium) {
        this.stadiumRepository.save(stadium);
    }

    @Override
    public void delete(Long id) {
        this.stadiumRepository.delete(id);
    }
}
