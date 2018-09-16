package app.email.services;

import app.models.SlotSignificationTime;
import app.models.User;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessionEmailService extends AbstractEmailService {

    private List<User> users;
    private List<SlotSignificationTime> slotSignificationTimes;

    public SessionEmailService() {
        super("NMFL");
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<SlotSignificationTime> getSlotSignificationTimes() {
        return slotSignificationTimes;
    }

    public void setSlotSignificationTimes(List<SlotSignificationTime> slotSignificationTimes) {
        this.slotSignificationTimes = slotSignificationTimes;
    }

    @Override
    protected String generateMessage() {
        StringBuilder message = new StringBuilder();
        if (slotSignificationTimes.isEmpty()) {
            return null;
        }
        slotSignificationTimes.forEach(slotSignificationTime -> {
            message.append(slotSignificationTime.getLeague().getName())
                    .append(" - с ").append(slotSignificationTime.getStartDate())
                    .append(" ").append(slotSignificationTime.getStartTime())
                    .append(" по ").append(slotSignificationTime.getEndDate())
                    .append(" ").append(slotSignificationTime.getEndTime()).append("\n");
        });

        return message.toString();
    }

    @Async
    public void sendMessageToEachUser() {
        users.forEach(user -> {
            send(user.getEmail());
        });

    }

}
