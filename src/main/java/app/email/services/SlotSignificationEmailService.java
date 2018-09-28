package app.email.services;

import app.models.Match;
import app.models.Slot;
import app.models.Stadium;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

@Service
public class SlotSignificationEmailService extends AbstractEmailService {
    private boolean isSignified;
    private String homeAndGuest;
    private String stadiumName;
    private String slotInfo;

    public SlotSignificationEmailService() {
        super("Сообщение о матче");
    }

    public void setSignified(boolean signified) {
        isSignified = signified;
    }

    public void setHomeAndGuest(String homeAndGuest) {
        this.homeAndGuest = homeAndGuest;
    }

    public void setStadiumName(String stadiumName) {
        this.stadiumName = stadiumName;
    }

    public void setSlotInfo(String slotInfo) {
        this.slotInfo = slotInfo;
    }

    @Override
    protected String generateMessage() {
        Context context = new Context();
        StringBuilder header = new StringBuilder("Новое сообщение о матче ");
        header.append(homeAndGuest);
        context.setVariable("header", header);
        StringBuilder text = new StringBuilder();
        if (isSignified) {
            text.append("Матч будет проводится в стадионе ");
            text.append(stadiumName);
            text.append(" в ");
            text.append(slotInfo);
        } else {
            text.append("Матч ");
            text.append(homeAndGuest);
            text.append(", назначенный на ");
            text.append(slotInfo);
            text.append(" в ");
            text.append("стадионе ");
            text.append(stadiumName);
            text.append(" был отменен");
        }
        text.append(".");
        context.setVariable("text", text);

        return templateEngine.process("email/slot_email", context);
    }
}
