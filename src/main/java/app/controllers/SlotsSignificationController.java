package app.controllers;

import app.email.MessageEmail;
import app.email.services.MessageEmailService;
import app.models.*;
import app.repositories.MatchRepository;
import app.repositories.UserRepository;
import app.services.MatchMessageServiceImpl;
import app.services.SlotsSignificationService;
import app.services.StadiumServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Controller
public class SlotsSignificationController {

    private final SlotsSignificationService slotsSignificationService;
    private final MatchRepository matchRepository;
    private final UserRepository userRepository;
    private final StadiumServiceImpl stadiumService;
    private final MatchMessageServiceImpl matchMessageService;
    private final MessageEmailService messageEmailService;

    @Autowired
    public SlotsSignificationController(SlotsSignificationService slotsSignificationService,
                                        MatchRepository matchRepository,
                                        UserRepository userRepository,
                                        StadiumServiceImpl stadiumService,
                                        MatchMessageServiceImpl matchMessageService, MessageEmailService messageEmailService) {
        this.slotsSignificationService = slotsSignificationService;
        this.matchRepository = matchRepository;
        this.userRepository = userRepository;
        this.stadiumService = stadiumService;
        this.matchMessageService = matchMessageService;
        this.messageEmailService = messageEmailService;
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
        String email;
        message.setMessage(text);
        message.setConsidered(false);
        Optional<Match> match = matchRepository.findById(id);
        if (match.isPresent()) {
            message.setMatch(match.get());
            matchMessageService.save(message);
            if(message.getConsidered()) {
                email = message.getMatch().getGuestTeam().getUser().getEmail();
            }

            else {
                email = message.getMatch().getHomeTeam().getUser().getEmail();
            }

            MessageEmail messageEmail = new MessageEmail();
            messageEmail.setMatchMessage(message);

            messageEmailService.setMessageEmail(messageEmail);
            messageEmailService.send(email);
        }

        return "";
    }

    @ResponseBody
    @RequestMapping(value = "/message/{id}/delete", method = RequestMethod.POST)
    public String deleteMessage(@PathVariable("id") Long id) {

        MatchMessage matchMessage = matchMessageService.findById(id);
        if (matchMessage != null) {
            Optional<Match> match = matchRepository.findById(matchMessage.getMatch().getId());
            match.ifPresent(match1 -> match1.setMatchMessage(null));
            matchMessageService.delete(id);
        }

        return "";
    }

}
