package app.email.services;

import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

@Service
public class ChangePasswordService extends AbstractEmailService {

    private String password;
    public ChangePasswordService() {
        super("Ваш пароль был изменен");
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    protected String generateMessage() {
        Context context = new Context();
        context.setVariable("password", password);

        return templateEngine.process("email/change_password", context);
    }
}
