package app.controllers;

import app.email.services.SessionEmailService;
import app.models.SlotSignificationTime;
import app.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class SessionController {

    private final SessionServiceImpl sessionService;
    private final LeagueServiceImpl leagueService;

    private final SlotsSignificationService significationService;
    private SessionEmailService sessionEmailService;
    private UserServiceImpl userService;

    @Autowired
    public SessionController(SessionServiceImpl sessionService, LeagueServiceImpl leagueService,
                             SlotsSignificationService significationService,
                             SessionEmailService sessionEmailService, UserServiceImpl userService) {
        this.sessionService = sessionService;
        this.leagueService = leagueService;

        this.significationService = significationService;
        this.sessionEmailService = sessionEmailService;
        this.userService = userService;
    }

    @RequestMapping(value = "/sessions", method = RequestMethod.GET)
    public String index(Model model) {

        model.addAttribute("sessions", sessionService.findAll());
        model.addAttribute("leagues", leagueService.findAll());

        return "sessions/index";
    }

    @RequestMapping(value = "/sessions/create", method = RequestMethod.GET)
    public String showCreateForm(Model model, SlotSignificationTime slotSignificationTime) {
        model.addAttribute("sessionModel", slotSignificationTime);
        model.addAttribute("leagues", leagueService.findAll());
        return "sessions/create";
    }

    @RequestMapping(value = "/sessions/save", method = RequestMethod.POST)
    public String saveSession(@ModelAttribute SlotSignificationTime slotSignificationTime, RedirectAttributes redirectAttributes) {

        if (slotSignificationTime.getEndDate().before(slotSignificationTime.getStartDate())) {
            redirectAttributes.addFlashAttribute("sessionModel", slotSignificationTime);
            redirectAttributes.addFlashAttribute("dateError", true);
            return "redirect:/sessions/create";
        }

        if (slotSignificationTime.getEndDate().equals(slotSignificationTime.getStartDate()) &&
                (slotSignificationTime.getEndDate().before(slotSignificationTime.getStartTime())
                || slotSignificationTime.getEndDate().equals(slotSignificationTime.getStartTime()))
        ) {
            redirectAttributes.addFlashAttribute("sessionModel", slotSignificationTime);
            redirectAttributes.addFlashAttribute("timeError", true);
            return "redirect:/sessions/create";
        }

        Optional<SlotSignificationTime> optional = sessionService.findAll().stream().filter(sT -> sT.getLeague()
                .equals(slotSignificationTime.getLeague())).findAny();


        if (optional.isPresent()) {

            optional.get().setStartDate(slotSignificationTime.getStartDate());
            optional.get().setEndDate(slotSignificationTime.getEndDate());
            optional.get().setStartTime(slotSignificationTime.getStartTime());
            optional.get().setEndTime(slotSignificationTime.getEndTime());

            sessionService.save(optional.get());

            return "redirect:/sessions";

        }

        sessionService.save(slotSignificationTime);

        return "redirect:/sessions";
    }

    @RequestMapping(value = "/sessions/{id}/edit", method = RequestMethod.GET)
    public String showEditForm(@PathVariable Long id, Model model) {
        SlotSignificationTime significationTime = sessionService.findById(id);
        model.addAttribute("sessionModel", significationTime);
        model.addAttribute("leagues", leagueService.findAll());

        return "sessions/edit";
    }

    @RequestMapping(value = "/sessions/{id}/update", method = RequestMethod.POST)
    public String updateSession(@PathVariable Long id, @ModelAttribute SlotSignificationTime sessionForm,
                                RedirectAttributes redirectAttributes) {

        if (sessionForm.getEndDate().before(sessionForm.getStartDate())) {
            redirectAttributes.addFlashAttribute("sessionModel", sessionForm);
            redirectAttributes.addFlashAttribute("dateError", true);
            return "redirect:/sessions/"+ id +"/edit";
        }

        if (sessionForm.getEndDate().equals(sessionForm.getStartDate()) &&
                (sessionForm.getEndTime().before(sessionForm.getStartTime())
                        || sessionForm.getEndTime().equals(sessionForm.getStartTime()))
        ) {
            redirectAttributes.addFlashAttribute("sessionModel", sessionForm);
            redirectAttributes.addFlashAttribute("timeError", true);
            return "redirect:/sessions/"+ id +"/edit";
        }

        SlotSignificationTime significationTime = sessionService.findById(id);

        Optional<SlotSignificationTime> optional = sessionService.findAll().stream().filter(sT -> sT.getLeague()
                .equals(sessionForm.getLeague()) && !sT.getLeague().equals(significationTime.getLeague())).findAny();

        if (optional.isPresent()) {

            optional.get().setStartDate(sessionForm.getStartDate());
            optional.get().setEndDate(sessionForm.getEndDate());
            optional.get().setStartTime(sessionForm.getStartTime());
            optional.get().setEndTime(sessionForm.getEndTime());

            sessionService.save(optional.get());

            return "redirect:/sessions";

        }


        significationTime.setStartDate(sessionForm.getStartDate());
        significationTime.setEndDate(sessionForm.getEndDate());
        significationTime.setStartTime(sessionForm.getStartTime());
        significationTime.setEndTime(sessionForm.getEndTime());
        significationTime.setLeague(sessionForm.getLeague());

        sessionService.save(significationTime);

        return "redirect:/sessions";
    }

    @RequestMapping(value = "sessions/{id}/delete", method = RequestMethod.POST)
    public String deleteSession(@PathVariable Long id, @ModelAttribute SlotSignificationTime significationTime) {
        sessionService.delete(id);

        return "redirect:/sessions";
    }

    @RequestMapping("/send-message")
    public String sendMessage() {
        sessionEmailService.setSlotSignificationTimes(significationService.getFutureSessions());
        sessionEmailService.setUsers(userService.findAll());
        sessionEmailService.sendMessageToEachUser();

        return "redirect:/sessions";
    }
}
