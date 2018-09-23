package app.email.services;

import app.models.SlotSignificationTime;
import app.models.User;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.List;

@Service
public class SessionEmailService extends AbstractEmailService {

    private List<User> users;
    private List<SlotSignificationTime> slotSignificationTimes;

    public SessionEmailService() {
        super("Оповещение о сессиях разбора");
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
        List<String> elements = new ArrayList<>();
        Context context = new Context();
        if (slotSignificationTimes.isEmpty()) {
            return null;
        }
        slotSignificationTimes.forEach(sst -> {
            String element = sst.getLeague().getName() + sst.getFormattedInterval();
            elements.add(element);
        });

        context.setVariable("elements", elements);

        return templateEngine.process("email/sessions", context);
    }

    @Async
    public void sendMessageToEachUser() {
        users.forEach(user -> {
            send(user.getEmail());
        });

    }

}
