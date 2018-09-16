package app.email.services;

import app.email.MessageEmail;
import app.models.Match;
import app.models.MatchMessage;
import org.springframework.stereotype.Service;

@Service
public class MessageEmailService extends AbstractEmailService {

    private MessageEmail messageEmail;

    public MessageEmailService() {
        super("NMFL");
    }

    public MessageEmail getMessageEmail() {
        return messageEmail;
    }

    public void setMessageEmail(MessageEmail messageEmail) {
        this.messageEmail = messageEmail;
    }

    @Override
    protected String generateMessage() {
        MatchMessage matchMessage = messageEmail.getMatchMessage();
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
