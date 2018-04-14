package app.controllers;

import app.models.Role;
import app.models.SlotSignificationTime;
import app.models.User;
import app.services.SlotsSignificationService;
import app.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;

@Controller
public class HomeController {

    private final UserServiceImpl userService;
    private final SlotsSignificationService slotsSignificationService;

    @Autowired
    public HomeController(UserServiceImpl userService, SlotsSignificationService slotsSignificationService) {
        this.userService = userService;
        this.slotsSignificationService = slotsSignificationService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model, Principal principal) {

        User user = userService.findByEmail(principal.getName());

        if (user.getRole().equals(Role.managerRole)) {
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
    public String changePassword(@PathVariable Long id, @RequestParam("new_password") String newPassword) {
        User user = userService.findById(id);
        BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
        user.setPassword(bCrypt.encode(newPassword));

        userService.save(user);

        return "redirect:/profile/" + id;
    }

}

