package app.controllers;

import app.dto.StadiumSlotsDTO;
import app.models.Stadium;
import app.models.User;
import app.services.StadiumService;
import app.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {

    private final StadiumService stadiumService;
    private final UserService userService;

    public HomeController(StadiumService stadiumService, UserService userService) {
        this.stadiumService = stadiumService;
        this.userService = userService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model, Principal principal) {

        User user = userService.findByEmail(principal.getName());
        List<StadiumSlotsDTO> stadiumsWithActualSlots = stadiumService.findAllWithSlotsByUser(user);
        model.addAttribute("stadiums", stadiumsWithActualSlots);

        return "index";
    }
}
