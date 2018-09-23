package app.email.services;

import app.email.RegistrationEmail;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

@Service
public class RegistrationEmailService extends AbstractEmailService {

    private RegistrationEmail registrationEmail;

    public RegistrationEmailService() {
        super("Вы были зарегистрированы в системе НМФЛ");
    }

    public RegistrationEmail getRegistrationEmail() {
        return registrationEmail;
    }

    public void setRegistrationEmail(RegistrationEmail registrationEmail) {
        this.registrationEmail = registrationEmail;
    }

    @Override
    protected String generateMessage() {
        String username = "Ваш логин: " + registrationEmail.getUserName();
        String password = "Ваш пароль: " + registrationEmail.getPassword();
        String header = "Вы были зарегистрированы в системе НМФЛ";
        Context context = new Context();
        context.setVariable("header", header);
        context.setVariable("username", username);
        context.setVariable("password", password);

        return templateEngine.process("email/registration", context);

    }
}
