package app.controllers;

import app.models.*;
import app.repositories.MatchRepository;
import app.repositories.SlotRepository;
import app.repositories.UserRepository;
import app.services.SlotMessageServiceImpl;
import app.services.SlotsSignificationService;
import app.services.StadiumServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class SlotsSignificationController {

    private final SlotsSignificationService slotsSignificationService;
    private final MatchRepository matchRepository;
    private final UserRepository userRepository;
    private final SlotRepository slotRepository;
    private final StadiumServiceImpl stadiumService;
    private final SlotMessageServiceImpl slotMessageService;

    @Autowired
    public SlotsSignificationController(SlotsSignificationService slotsSignificationService,
                                        MatchRepository matchRepository,
                                        UserRepository userRepository,
                                        SlotRepository slotRepository,
                                        StadiumServiceImpl stadiumService,
                                        SlotMessageServiceImpl slotMessageService) {
        this.slotsSignificationService = slotsSignificationService;
        this.matchRepository = matchRepository;
        this.userRepository = userRepository;
        this.slotRepository = slotRepository;
        this.stadiumService = stadiumService;
        this.slotMessageService = slotMessageService;
    }

    @RequestMapping(value = "stadium/{id}", method = RequestMethod.GET)
    public String showStadiumSlots(@PathVariable Long id, Model model,
                                   HttpServletRequest httpServletRequest) {
        String resultJSON = slotsSignificationService
                .generateSlotsJSON(id, httpServletRequest.getUserPrincipal().getName());
        model.addAttribute("slots", resultJSON);
        model.addAttribute("stadiumsList", stadiumService.findAllWithSlots());
        model.addAttribute("currentStadium", stadiumService.findById(id));
        return "slots_signification/index";
    }

    @RequestMapping(value = "/stadium/{id}/signify/{slotId}", method = RequestMethod.GET)
    public String showCreateForm(@PathVariable Long id, @PathVariable Long slotId,
                                 Model model, HttpServletRequest httpServletRequest,
                                 MatchForm matchForm) {
        List<Match> actualMatches = slotsSignificationService.getActualMatches(httpServletRequest, slotId);
        User currUser = userRepository.findByEmail(httpServletRequest.getUserPrincipal().getName());
        List<Match> matchesWithMessages = slotsSignificationService.getMatchesWithMessages(currUser);

        if (!matchesWithMessages.isEmpty()) {
            actualMatches = matchesWithMessages;
        }

        model.addAttribute("matches", actualMatches);
        model.addAttribute("id", id);
        model.addAttribute("slotId", slotId);
        model.addAttribute("matchForm", matchForm);

        return "slots_signification/signify";
    }

    @RequestMapping(value = "stadium/{id}/signify/{slotId}", method = RequestMethod.POST)
    public String signifySlot(@PathVariable Long id, @PathVariable Long slotId,
                              @ModelAttribute MatchForm matchForm, HttpServletRequest httpServletRequest) {
        if (matchForm.getId() != null) {
            slotsSignificationService.signifySlot(matchForm.getId(), slotId, httpServletRequest.getUserPrincipal().getName());
        }
        return "redirect:/stadium/" + id;
    }

    @RequestMapping(value = "stadium/{id}/reject/{slotId}", method = RequestMethod.GET)
    public String rejectSlot(@PathVariable Long id, @PathVariable Long slotId, HttpServletRequest httpServletRequest) {
        slotsSignificationService.rejectSlot(slotId, httpServletRequest.getUserPrincipal().getName());
        return "redirect:/stadium/" + id;
    }

    @ResponseBody
    @RequestMapping(value = "/save-message/{id}", method = RequestMethod.POST)
    public String saveMessage(@PathVariable("id") Long id, @RequestParam String text) {
        MatchMessage message = new MatchMessage();
        message.setMessage(text);
        message.setConsidered(false);
        message.setMatch(matchRepository.findOne(id));
        slotMessageService.save(message);
        return "";
    }

    @ResponseBody
    @RequestMapping(value = "/message/{id}/delete", method = RequestMethod.POST)
    public String deleteMessage(@PathVariable("id") Long id) {

        Match match = matchRepository.findOne(slotMessageService.findById(id).getMatch().getId());
        match.setMatchMessage(null);
        slotMessageService.delete(id);

        return "";
    }

}
