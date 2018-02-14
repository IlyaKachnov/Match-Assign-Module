package app.controllers;

import app.models.SlotSignificationTime;
import app.services.LeagueService;
import app.services.LeagueServiceImpl;
import app.services.SessionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SessionController {

    private final SessionServiceImpl sessionService;
    private final LeagueServiceImpl leagueService;

    @Autowired
    public SessionController(SessionServiceImpl sessionService, LeagueServiceImpl leagueService) {
        this.sessionService = sessionService;
        this.leagueService = leagueService;
    }

    @RequestMapping(value = "/sessions", method = RequestMethod.GET)
    public String index(Model model)
    {
        model.addAttribute("sessions", sessionService.findAll());

        return "sessions/index";
    }

    @RequestMapping(value = "/sessions/create", method = RequestMethod.GET)
    public String showCreateForm(Model model, SlotSignificationTime slotSignificationTime)
    {
        model.addAttribute("sessionModel", slotSignificationTime);
        model.addAttribute("leagues",leagueService.findAll());
        return "sessions/create";
    }

    @RequestMapping(value = "sessions/save", method = RequestMethod.POST)
    public String saveStadium(@ModelAttribute  SlotSignificationTime slotSignificationTime)
    {
        sessionService.save(slotSignificationTime);

        return "redirect:/sessions";
    }

    @RequestMapping(value = "session/{id}/delete", method = RequestMethod.POST)
    public String deleteSession(@PathVariable Long id)
    {
        sessionService.delete(id);

        return "redirect:/sessions";
    }
}
