package app.services;

import app.models.SlotMessage;

public interface EmailService {
    void sendMessage();
    void sendNotification(String email, SlotMessage slotMessage);
}
