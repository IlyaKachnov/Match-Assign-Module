package app.email.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

public abstract class AbstractEmailService {

    private static final Logger logger = LoggerFactory.getLogger(AbstractEmailService.class);

    @Value("${spring.mail.username}")
    protected String from;
    protected String subject;

    @Autowired
    protected JavaMailSender mailSender;

    @Autowired
    protected TemplateEngine templateEngine;

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
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setTo(userName);
            messageHelper.setSubject(subject);
            messageHelper.setText(messageText, true);
            messageHelper.setFrom(from, "НМФЛ");
        };
        try {
            mailSender.send(messagePreparator);
            System.out.println("message sent");
        } catch (Exception e) {
            logger.error("Error while sending message", e);
        }
    }

    protected abstract String generateMessage();

}
