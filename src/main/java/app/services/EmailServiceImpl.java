package app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final String messageTemplate = "Добрый день!\nРаспределение слотов состоится в следующие дни:\n%s\nС уважением, команда НМФЛ.";

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Scheduled(fixedRate = 5000)
    public void sendMessage() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("evgeni-trofimo@yandex.ru");
        message.setSubject("NMFL");
        message.setText(String.format(messageTemplate, "Высшая Лига - 28.02.18 с 17:00 по 19:00"));
        message.setFrom("nmfl2018@mail.ru");
        mailSender.send(message);
    }
}
