package app.email.services;

import app.models.Match;
import app.models.MatchMessage;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;


@Service
public class MessageEmailService extends AbstractEmailService {

    private MatchMessage message;
    private Match match;
    private String homeAndGuest;

    public MessageEmailService() {
        super("Новое сообщение от менеджера команды");
    }

    public void setMessage(MatchMessage message) {
        this.message = message;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public void setHomeAndGuest(String homeAndGuest) {
        this.homeAndGuest = homeAndGuest;
    }

    @Override
    protected String generateMessage() {
        boolean status = message.getConsidered();
        Context context = new Context();
//        String homeAndGuest = match.getHomeTeam() + " - " + match.getGuestTeam();
        StringBuilder header = new StringBuilder("Новое сообщение о матче ");
//        System.out.println(message.getMatch().getGuestTeam().getName());
        header.append(homeAndGuest);
        header.append(" ");
        header.append("назначенном на ");
        header.append(match.getFormattedDate());
        header.append(".");

        StringBuilder text = new StringBuilder();
        context.setVariable("header", header.toString());
        if (status) {
            text.append("Матч был перенесен на дату: ");
            text.append(match.getFormattedDate());
        }

        else {
            text.append("Сообщение:");
            text.append(message.getMessage());

        }
        context.setVariable("text", text.toString());
        return templateEngine.process("email/message", context);
    }

}
