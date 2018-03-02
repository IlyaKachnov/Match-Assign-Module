package app.controllers;

import app.models.League;
import app.models.SlotSignificationTime;
import app.services.EmailServiceImpl;
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
    private final EmailServiceImpl emailService;

    @Autowired
    public SessionController(SessionServiceImpl sessionService, LeagueServiceImpl leagueService, EmailServiceImpl emailService) {
        this.sessionService = sessionService;
        this.leagueService = leagueService;
        this.emailService = emailService;

    }

    @RequestMapping(value = "/sessions", method = RequestMethod.GET)
    public String index(Model model)
    {

        model.addAttribute("sessions", sessionService.findAll());
        model.addAttribute("leagues", leagueService.findAll());

        return "sessions/index";
    }

    @RequestMapping(value = "/sessions/create", method = RequestMethod.GET)
    public String showCreateForm(Model model, SlotSignificationTime slotSignificationTime)
    {
        model.addAttribute("sessionModel", slotSignificationTime);
        model.addAttribute("leagues",leagueService.findAll());
        return "sessions/create";
    }

    @RequestMapping(value = "/sessions/save", method = RequestMethod.POST)
    public String saveSession(@ModelAttribute  SlotSignificationTime slotSignificationTime)
    {
        League league = leagueService.findById(slotSignificationTime.getLeague().getId());

        if(league.getSlotSignificationTime() != null) {
            sessionService.delete(league.getSlotSignificationTime().getId());
        }

        sessionService.save(slotSignificationTime);

        return "redirect:/sessions";
    }

    @RequestMapping(value = "sessions/{id}/delete", method = RequestMethod.POST)
    public String deleteSession(@PathVariable Long id)
    {
        sessionService.delete(id);

        return "redirect:/sessions";
    }

    @RequestMapping("/send-message")
    public String sendMessage()
    {
        emailService.sendMessage();

        return "redirect:/sessions";
    }
}
