package app.services;

import app.models.SlotSignificationTime;
import app.models.Team;
import app.models.User;
import app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@Primary

public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final SlotsSignificationService slotsSignificationService;
    @Autowired
    public UserServiceImpl(UserRepository userRepository, SlotsSignificationService slotsSignificationService) {
        this.slotsSignificationService = slotsSignificationService;
        this.userRepository= userRepository;
    }

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

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<SlotSignificationTime> getActualNotifications(User user){
        List<Team> teamList = user.getTeamList();
        if(teamList.isEmpty())
        {
            return null;
        }
        List<SlotSignificationTime> actualNotifications = new ArrayList<>();
        teamList.forEach(team -> {
            SlotSignificationTime slotSignificationTime = team.getLeague().getSlotSignificationTime();
            if (slotSignificationTime == null)
            {
                return;
            }
            if(slotsSignificationService.checkDateTime(slotSignificationTime)){
                actualNotifications.add(slotSignificationTime);
            }
        });

        return actualNotifications;
    }
}
