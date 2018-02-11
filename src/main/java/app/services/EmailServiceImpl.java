package app.services;

import app.models.SlotSignificationTime;
import app.models.User;
import app.repositories.SlotSignificationTimeRepository;
import app.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender mailSender;
    private final SlotSignificationTimeRepository slotSignificationTimeRepository;
    private final UserRepository userRepository;
    private final String messageTemplate = "Добрый день!\nРаспределение слотов состоится в следующие дни:\n%s\nС уважением, команда НМФЛ.";

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender,
                            SlotSignificationTimeRepository slotSignificationTimeRepository,
                            UserRepository userRepository) {
        this.mailSender = mailSender;
        this.slotSignificationTimeRepository = slotSignificationTimeRepository;
        this.userRepository = userRepository;
    }

//    @Scheduled(fixedRate = 5000)
    public void sendMessage() {
        StringBuilder significationTimes = new StringBuilder();
        List<SlotSignificationTime> slotSignificationTimes = slotSignificationTimeRepository.findAll();
        if (slotSignificationTimes.isEmpty()) {
            logger.info("Nothing to send");
            return;
        }
        slotSignificationTimes.forEach(slotSignificationTime -> {
            significationTimes.append(slotSignificationTime.getLeague().getName())
                    .append(" - с ").append(slotSignificationTime.getStartDate())
                    .append(" ").append(slotSignificationTime.getStartTime())
                    .append(" по ").append(slotSignificationTime.getEndDate())
                    .append(" ").append(slotSignificationTime.getEndTime()).append("\n");
        });
        sendMessageToEachUser(String.format(messageTemplate, significationTimes));
    }

    private void sendMessageToEachUser(String messageText) {
        List<User> users = userRepository.findAll();
        users.forEach(user -> {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(user.getEmail());
            message.setSubject("NMFL");
            message.setText(messageText);
            message.setFrom("nmfl2018@mail.ru");
            mailSender.send(message);
//            System.out.println("message sent");
        });
    }
}
