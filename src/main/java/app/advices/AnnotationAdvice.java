package app.advices;

import app.dto.NotificationDTO;
import app.models.MatchMessage;
import app.models.Role;
import app.models.User;
import app.services.MatchMessageServiceImpl;
import app.services.SlotsSignificationService;
import app.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice(annotations = Controller.class)
public class AnnotationAdvice {

    private UserServiceImpl userService;
    private SlotsSignificationService slotsSignificationService;
    private MatchMessageServiceImpl matchMessageService;

    @Autowired
    public AnnotationAdvice(UserServiceImpl userService, SlotsSignificationService slotsSignificationService,
                            MatchMessageServiceImpl matchMessageService) {
        this.userService = userService;
        this.slotsSignificationService = slotsSignificationService;
        this.matchMessageService = matchMessageService;
    }

    @ModelAttribute("notifications")
    public List<NotificationDTO> getNotifications(Principal principal) {
        User user = userService.findByEmail(principal.getName());
        if (user.getRole().equals(Role.adminRole)) {
            return slotsSignificationService.getNotifications();
        }

        return slotsSignificationService.getNotifications(user);
    }

    @ModelAttribute("allMessages")
    public List<MatchMessage> getMatchMessages(Principal principal) {
        User user = userService.findByEmail(principal.getName());
        List<MatchMessage> matchMessages = new ArrayList<>();
        if (user.getRole().equals(Role.managerRole)) {
            matchMessages = matchMessageService.getMessagesForUser(user);
        }
        return matchMessages;
    }
}
