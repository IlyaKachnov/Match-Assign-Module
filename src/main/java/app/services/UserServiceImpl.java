package app.services;

import app.models.SlotSignificationTime;
import app.models.Team;
import app.models.User;
import app.repositories.TeamRepository;
import app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@Primary

public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final SlotsSignificationService slotsSignificationService;
    private final TeamRepository teamRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, SlotsSignificationService slotsSignificationService, TeamRepository teamRepository) {
        this.slotsSignificationService = slotsSignificationService;
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
    }

    @Override
    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


}
