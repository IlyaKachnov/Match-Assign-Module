package app.controllers;

import app.models.MatchMessage;
import app.models.Role;
import app.models.User;
import app.services.MatchMessageService;
import app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
        if (user.getRole().equals(Role.adminRole)) {
            return "error/404";
        }
        if (user.getRole().equals(Role.managerRole)) {
            List<MatchMessage> matchMessages = matchMessageService.getMessagesForUser(user);
            matchMessages = matchMessages.stream().filter(matchMessage ->
                    matchMessage.getLocalDateTime().plus(2, ChronoUnit.DAYS).isAfter(LocalDateTime.now()
                    )).collect(Collectors.toList());

            model.addAttribute("messages", matchMessages);
        }
        return "messages/index";
    }

    //TODO:система нотификаций в будущей версии
//    @GetMapping(value = "messages/{id}/read")
//    public String setReadStatus(@PathVariable("id") Long id, Principal principal) {
//        User user = userService.findByEmail(principal.getName());
//        if (user.getRole().equals(Role.adminRole)) {
//            return "error/404";
//        }
//        MatchMessage matchMessage = matchMessageService.findById(id);
//        matchMessage.setRead(true);
//        matchMessageService.save(matchMessage);
//        return "redirect:/messages";
//    }
}
