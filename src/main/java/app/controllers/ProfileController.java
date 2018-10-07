package app.controllers;

import app.email.services.ChangePasswordService;
import app.models.User;
import app.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {

    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);
    private final UserService userService;
    private final ChangePasswordService changePasswordService;

    @Autowired
    public ProfileController(UserService userService,
                             ChangePasswordService changePasswordService) {
        this.userService = userService;
        this.changePasswordService = changePasswordService;
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

