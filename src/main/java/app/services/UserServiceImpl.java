package app.services;

import app.models.User;
import app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary

public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> findAll()
    {
        return this.userRepository.findAll();
    }

    @Override
    public void save(User user)
    {
        userRepository.save(user);
    }

    @Override
    public User findById(Long id)
    {
        return userRepository.findOne(id);
    }

    @Override
    public void delete(Long id)
    {
        userRepository.delete(id);
    }
}
