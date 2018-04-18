package app.services;

import app.models.*;
import app.repositories.SlotSignificationTimeRepository;
import app.repositories.UserRepository;
import com.sun.mail.smtp.SMTPSenderFailedException;
import org.codehaus.groovy.control.messages.SimpleMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender mailSender;
    private final SlotSignificationTimeRepository slotSignificationTimeRepository;
    private final UserRepository userRepository;
    private final SlotsSignificationService slotsSignificationService;
    private final String messageTemplate = "Добрый день!\nРаспределение слотов состоится в следующие дни:\n%s\nС уважением, команда НМФЛ.";

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender,
                            SlotSignificationTimeRepository slotSignificationTimeRepository,
                            UserRepository userRepository, SlotsSignificationService slotsSignificationService) {
        this.mailSender = mailSender;
        this.slotSignificationTimeRepository = slotSignificationTimeRepository;
        this.userRepository = userRepository;
        this.slotsSignificationService = slotsSignificationService;

    }

    //    @Scheduled(fixedRate = 5000)
    @Override
    public void sendMessage() {
        StringBuilder significationTimes = new StringBuilder();
        List<SlotSignificationTime> slotSignificationTimes = slotsSignificationService.getFutureSessions();
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
            } catch (MailSendException e) {
                e.getMessage();
            }

        });

    }

    @Override
    public void sendNotification(String email, SlotMessage slotMessage) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Новое сообщение от НМФЛ");
        message.setText(buildNotification(slotMessage));
        message.setFrom("nmfl2018@mail.ru");
        mailSender.send(message);

    }

    private String buildNotification(SlotMessage slotMessage){
        Match match = slotMessage.getMatch();
        String guestTeamName = match.getGuestTeam().getName();
        StringBuilder stringBuilder = new StringBuilder("Сообщение от менеджера команды ");
        stringBuilder.append(guestTeamName)
                .append(" о матче ")
                .append(match.getHomeAndGuest())
                .append(" назначенном на ")
                .append(match.getFormattedDate())
                .append(": \n")
                .append(slotMessage.getMessage());

        return stringBuilder.toString();
    }
}
