package app.email.services;

import app.email.RegistrationEmail;
import org.springframework.stereotype.Service;

@Service
public class RegistrationEmailService extends AbstractEmailService {

    private final String messageTemplate = "Добрый день!\nРаспределение слотов состоится в следующие дни:\n%s\nС уважением, команда НМФЛ.";
    private RegistrationEmail registrationEmail;

    public RegistrationEmailService() {
        super("NMFL");
    }

    public RegistrationEmail getRegistrationEmail() {
        return registrationEmail;
    }

    public void setRegistrationEmail(RegistrationEmail registrationEmail) {
        this.registrationEmail = registrationEmail;
    }

    @Override
    protected String generateMessage() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Ваш логин: ").append(registrationEmail.getUserName())
                .append("Ваш пароль: ").append(registrationEmail.getPassword());
        return stringBuilder.toString();
    }
}
