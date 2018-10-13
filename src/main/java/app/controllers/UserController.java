package app.controllers;

import app.email.RegistrationEmail;
import app.email.services.RegistrationEmailService;
import app.models.Role;
import app.models.Team;
import app.services.TeamServiceImpl;
import app.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import app.models.User;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    private UserServiceImpl userService;
    private TeamServiceImpl teamService;
    private RegistrationEmailService registrationEmailService;

    @Autowired
    public UserController(UserServiceImpl userService, TeamServiceImpl teamService,
                          RegistrationEmailService registrationEmailService) {
        this.userService = userService;
        this.teamService = teamService;
        this.registrationEmailService = registrationEmailService;
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("allUsers", userService.findAll());

        return "users/index";
    }

    @RequestMapping(value = "/users/create", method = RequestMethod.GET)
    public String showCreateForm(Model model, User user) {

        model.addAttribute("user", user);
        model.addAttribute("teamList", teamService.findAllOrderByName());

        return "users/create";

    }

    @RequestMapping(value = "/users/save", method = RequestMethod.POST)
    public String saveUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        if (userService.findByEmail(user.getEmail()) != null) {
            redirectAttributes.addFlashAttribute("user", user);
            redirectAttributes.addFlashAttribute("error", true);

            return "redirect:/users/create";
        }

        BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();

        User userModel = new User();
        userModel.setFirstname(user.getFirstname());
        userModel.setLastname(user.getLastname());
        userModel.setEmail(user.getEmail());
        userModel.setRole(user.getRole());
        String password = user.getPassword();
        userModel.setPassword(bCrypt.encode(password));

        userService.save(userModel);

        if (user.getRole().equals(Role.managerRole) && user.getTeamList() != null) {
            user.getTeamList().forEach(team -> {
                team.setUser(userModel);
                teamService.save(team);
            });
        }

        RegistrationEmail registrationEmail = new RegistrationEmail();
        registrationEmail.setUserName(userModel.getEmail());
        registrationEmail.setPassword(user.getPassword());
        registrationEmailService.setRegistrationEmail(registrationEmail);
        registrationEmailService.send(userModel.getEmail());

        return "redirect:/users";
    }


    @RequestMapping(value = "/users/{id}/edit", method = RequestMethod.GET)
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        User user = userService.findById(id);

        model.addAttribute("user", user);
        model.addAttribute("teamList", teamService.findAllOrderByName());
        return "users/edit";
    }

    @RequestMapping(value = "/users/{id}/update", method = RequestMethod.POST)
    public String updateUser(@PathVariable("id") Long id, @ModelAttribute User user, RedirectAttributes redirectAttributes) {
        User userModel = userService.findById(id);
        if (userService.findByEmail(user.getEmail()) != null && (!user.getEmail().equals(userModel.getEmail()))) {
            redirectAttributes.addFlashAttribute("user", user);
            redirectAttributes.addFlashAttribute("error", true);

            return "redirect:/users/" + id + "/edit";
        }

        userModel.setFirstname(user.getFirstname());
        userModel.setLastname(user.getLastname());
        userModel.setEmail(user.getEmail());
        userModel.setRole(user.getRole());

        userService.save(userModel);

        List<Team> oldList = user.getTeamList();
        List<Team> newList = userModel.getTeamList();


        for (Team team : newList) {
            team.setUser(null);
            teamService.save(team);
        }
        if (user.getRole().equals(Role.managerRole)) {
            for (Team team : oldList) {
                team.setUser(userModel);
                teamService.save(team);
            }
        }
        return "redirect:/users";
    }

    @RequestMapping(value = "/users/{id}/delete", method = RequestMethod.GET)
    public String deleteUser(@PathVariable("id") Long id, @ModelAttribute User user) {
        User userModel = userService.findById(id);

        for (Team team : userModel.getTeamList()) {
            team.setUser(null);
            teamService.save(team);
        }
        userService.delete(id);

        return "redirect:/users";
    }


}
