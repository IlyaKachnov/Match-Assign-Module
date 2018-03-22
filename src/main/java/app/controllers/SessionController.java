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

    @RequestMapping(value = "/sessions/{id}/edit", method = RequestMethod.GET)
    public String showEditForm(@PathVariable Long id,  Model model) {
        SlotSignificationTime significationTime = sessionService.findById(id);
        model.addAttribute("sessionModel",significationTime);
        model.addAttribute("leagues",leagueService.findAll());

        return "sessions/edit";
    }

    @RequestMapping(value = "/sessions/{id}/update", method = RequestMethod.POST)
    public String updateSession(@PathVariable Long id, @ModelAttribute SlotSignificationTime sessionForm){
        SlotSignificationTime significationTime = sessionService.findById(id);

        significationTime.setStartDate(sessionForm.getStartDate());
        significationTime.setEndDate(sessionForm.getEndDate());
        significationTime.setStartTime(sessionForm.getStartTime());
        significationTime.setEndTime(sessionForm.getEndTime());
        significationTime.setLeague(sessionForm.getLeague());

        sessionService.save(significationTime);

        return "redirect:/sessions";
    }

    @RequestMapping(value = "sessions/{id}/delete", method = RequestMethod.POST)
    public String deleteSession(@PathVariable Long id, @ModelAttribute SlotSignificationTime significationTime)
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
