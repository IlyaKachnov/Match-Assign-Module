package app.controllers;

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

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    UserServiceImpl userService;

    @Resource
    TeamServiceImpl teamService;
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String index(Model model)
    {
        model.addAttribute("allUsers", userService.findAll());

        return "users/index";
    }

    @RequestMapping(value = "/users/create", method = RequestMethod.GET)
    public String showCreateForm(Model model, User user) {

        model.addAttribute("user", user);
        model.addAttribute("teamList", teamService.findAll());

        return "users/create";

    }

    @RequestMapping(value = "/users/save", method = RequestMethod.POST)
    public String saveUser(@ModelAttribute User user, User userModel)
    {
        BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();

        List<Team> selectedTeams = new ArrayList<>();
        userModel.setFirstname(user.getFirstname());
        userModel.setLastname(user.getLastname());
        userModel.setEmail(user.getEmail());
        userModel.setRole(user.getRole());

        String password = user.getPassword();
        userModel.setPassword(bCrypt.encode(password));

        List<User> associatedUsers = new ArrayList<>();
        associatedUsers.add(userModel);
        for (Team t: user.getTeamList()) {
            selectedTeams.add(t);
            t.setUsers(associatedUsers);
        }
        userModel.setTeamList(selectedTeams);

        userService.save(userModel);

        return "redirect:/users";
    }


@RequestMapping(value = "/users/{id}/edit", method = RequestMethod.GET)
public String showEditForm(@PathVariable("id") Long id, Model model)
{
    User user = userService.findById(id);
    model.addAttribute("user", user);

    return "users/edit";
}

    @RequestMapping(value = "/users/{id}/update", method = RequestMethod.POST)
    public String updateUser(@PathVariable("id") Long id, @ModelAttribute User user)
    {
        User userModel = userService.findById(id);
        userModel.setFirstname(user.getFirstname());
        userModel.setLastname(user.getLastname());
        userModel.setEmail(user.getEmail());
        userModel.setRole(user.getRole());
//        userModel.setTeamList(user.getTeamList());

//        userModel.setPassword(user.getPassword());

        userService.save(userModel);

        return "redirect:/users";
    }

    @RequestMapping(value = "/users/{id}/delete", method = RequestMethod.POST)
    public String deleteUser(@PathVariable("id") Long id, @ModelAttribute User user)
    {

        userService.delete(id);

        return "redirect:/users";
    }


}
