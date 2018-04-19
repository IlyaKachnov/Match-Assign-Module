package app.services;

import app.models.SlotMessage;
import app.models.SlotSignificationTime;

import java.util.List;

public interface EmailService {
    void sendMessage(List<SlotSignificationTime> slotSignificationTimes);
    void sendNotification(String email, SlotMessage slotMessage);
}
