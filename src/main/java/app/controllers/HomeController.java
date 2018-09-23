package app.controllers;

import app.email.services.ChangePasswordService;
import app.models.MatchMessage;
import app.models.Role;
import app.models.User;
import app.services.MatchMessageService;
import app.services.SlotsSignificationService;
import app.services.UserService;
import app.services.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    private final UserServiceImpl userService;
    private final SlotsSignificationService slotsSignificationService;
    private final MatchMessageService matchMessageService;
    private final ChangePasswordService changePasswordService;

    @Autowired
    public HomeController(UserServiceImpl userService, SlotsSignificationService slotsSignificationService,
                          MatchMessageService matchMessageService, ChangePasswordService changePasswordService) {
        this.userService = userService;
        this.slotsSignificationService = slotsSignificationService;
        this.matchMessageService = matchMessageService;
        this.changePasswordService = changePasswordService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model, Principal principal) {

        User user = userService.findByEmail(principal.getName());

        if (user.getRole().equals(Role.managerRole)) {
            List<MatchMessage> matchMessages = matchMessageService.getMessagesForHomeTeam(user);
                List<MatchMessage> messages = matchMessages.stream().filter(matchMessage ->
                        matchMessage.getMatch().getMatchDate().after(new Date())).collect(Collectors.toList());
            model.addAttribute("messages", messages);
            model.addAttribute("teamList", user.getTeamList());
            model.addAttribute("notifications", slotsSignificationService.getActualSessions(user));
            model.addAttribute("futureSessions", slotsSignificationService.getFutureSessions(user));

            return "index";
        }

        model.addAttribute("notifications", slotsSignificationService.getActualSessions());
        model.addAttribute("futureSessions", slotsSignificationService.getFutureSessions());

        return "index";
    }

    @RequestMapping(value = "/profile/{id}", method = RequestMethod.GET)
    public String showProfile(@PathVariable Long id, Model model) {
        model.addAttribute("userForm", userService.findById(id));

        return "profile";
    }

    @RequestMapping(value = "/profile/{id}/update", method = RequestMethod.POST)
    public String updateProfile(@PathVariable Long id, @ModelAttribute User userForm) {
        User user = userService.findById(id);
        user.setFirstname(userForm.getFirstname());
        user.setLastname(userForm.getLastname());

        userService.save(user);

        return "redirect:/profile/" + id;
    }

    @RequestMapping(value = "/change-password/{id}", method = RequestMethod.POST)
    public String changePassword(@PathVariable Long id, @RequestParam("new_password") String newPassword,
                                 HttpServletRequest httpServletRequest) {
        User user = userService.findById(id);
        BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
        user.setPassword(bCrypt.encode(newPassword));

        userService.save(user);

        changePasswordService.setPassword(newPassword);
        changePasswordService.send(httpServletRequest.getUserPrincipal().getName());


        return "redirect:/profile/" + id;
    }

}

