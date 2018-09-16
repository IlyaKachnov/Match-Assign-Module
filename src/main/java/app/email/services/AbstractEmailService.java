package app.email.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;

public abstract class AbstractEmailService {

    private static final Logger logger = LoggerFactory.getLogger(AbstractEmailService.class);

    @Value("${spring.mail.username}")
    protected String from;
    protected String subject;

    @Autowired
    protected JavaMailSender mailSender;

    public AbstractEmailService(String subject) {
        this.subject = subject;
    }

    @Async
    public void send(String userName) {
        String messageText = generateMessage();
        if (messageText == null) {
            logger.info("Nothing to send");
            return;
        }
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(userName);
            message.setSubject(subject);
            message.setText(messageText);
            message.setFrom(from);
            mailSender.send(message);
            System.out.println("message sent");
        } catch (Exception e) {
            logger.error("Error while sending message", e);
        }
    }

    protected abstract String generateMessage();

}
