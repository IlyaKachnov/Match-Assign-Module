package app.services;

import app.models.*;
import app.repositories.SlotSignificationTimeRepository;
import app.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
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

    @Async
    @Override
    public void sendMessage(List<SlotSignificationTime> slotSignificationTimes) {
        StringBuilder significationTimes = new StringBuilder();
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
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(user.getEmail());
                message.setSubject("NMFL");
                message.setText(messageText);
                message.setFrom("nmfl2018@mail.ru");
                mailSender.send(message);
                System.out.println("message sent");
            } catch (Exception e) {
                logger.error("Error while sending message", e);
            }

        });

    }

    @Async
    @Override
    public void sendNotification(String email, MatchMessage matchMessage) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Новое сообщение от НМФЛ");
            message.setText(buildNotification(matchMessage));
            message.setFrom("nmfl2018@mail.ru");
            mailSender.send(message);
        } catch (Exception e) {
            logger.error("Error while sending message", e);
        }

    }

    private String buildNotification(MatchMessage matchMessage) {
        Match match = matchMessage.getMatch();
        String msg = "Сообщение от менеджера команды ";
        boolean status = matchMessage.getConsidered();

        if (status) {
            msg += match.getHomeTeam().getName()
                    + ": \n"
                    + " матч "
                    + matchMessage.getMatch().getHomeAndGuest()
                    + " был перенесен на "
                    + matchMessage.getMatch().getFormattedDate();

            return msg;
        }
        msg += match.getGuestTeam().getName()
                + " о матче "
                + match.getHomeAndGuest()
                + " назначенном на "
                + match.getFormattedDate()
                + ": \n"
                + matchMessage.getMessage();

        return msg;
    }
}
