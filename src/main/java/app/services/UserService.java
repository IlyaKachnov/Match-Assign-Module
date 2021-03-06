package app.services;

import app.models.User;

import java.util.List;

public interface UserService {

    User findById(Long id);
    List<User> findAll();
    void save(User user);
    void delete(Long id);
    User findByEmail(String email);
    User findByResetToken(String resetToken);
}
