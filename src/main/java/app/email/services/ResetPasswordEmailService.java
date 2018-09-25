package app.email.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;

@Service
public class ResetPasswordEmailService extends AbstractEmailService{

    private String resetUrl;

    public ResetPasswordEmailService() {
        super("Запрос на восстановление пароля");
    }

    public String getResetUrl() {
        return resetUrl;
    }

    public void setResetUrl(String resetUrl) {
        this.resetUrl = resetUrl;
    }

    @Override
    protected String generateMessage() {
        Context context = new Context();
        context.setVariable("url", resetUrl);
        return templateEngine.process("email/reset_password", context);
    }
}
