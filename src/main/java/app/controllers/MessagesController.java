package app.controllers;

import app.models.MatchMessage;
import app.models.Role;
import app.models.User;
import app.services.MatchMessageService;
import app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MessagesController {

    private final MatchMessageService matchMessageService;
    private final UserService userService;

    @Autowired
    public MessagesController(MatchMessageService matchMessageService, UserService userService) {
        this.matchMessageService = matchMessageService;
        this.userService = userService;
    }

    @RequestMapping(value = "/messages", method = RequestMethod.GET)
    public String getMessages(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());

        if (user.getRole().equals(Role.managerRole)) {
            List<MatchMessage> matchMessages = matchMessageService.getMessagesForUser(user);
            List<MatchMessage> messages = new ArrayList<>();
            if (matchMessages != null && !matchMessages.isEmpty()) {
                messages = matchMessages.stream().filter(matchMessage ->
                        matchMessage.getMatch().getMatchDate() == null ||
                                matchMessage.getMatch().getMatchDate().after(new Date()))
                        .collect(Collectors.toList());
            }
            model.addAttribute("messages", messages);
        }
        return "messages/index";
    }
}