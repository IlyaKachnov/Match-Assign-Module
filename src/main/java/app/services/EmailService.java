package app.services;

import app.models.MatchMessage;
import app.models.SlotSignificationTime;

import java.util.List;

public interface EmailService {
    void sendMessage(List<SlotSignificationTime> slotSignificationTimes);
    void sendNotification(String email, MatchMessage matchMessage);
}
