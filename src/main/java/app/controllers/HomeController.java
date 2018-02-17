package app.controllers;

import app.models.SlotSignificationTime;
import app.models.User;
import app.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.time.LocalDate;

@Controller
public class HomeController {
    @Autowired
    UserServiceImpl userService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model, Principal principal) {

        User user = userService.findByEmail(principal.getName());
        model.addAttribute("teamList", user.getTeamList());
        model.addAttribute("notifications", userService.getActualNotifications(user));

//        for (SlotSignificationTime st: userService.getActualNotifications(user)) {
//            System.out.println(st.getLeague().getName());
//        }
//        System.exit(1);
        return "index";
    }

    @RequestMapping("/403")
    public String accessDenied() {
        return "errors/403";
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

}

