package app.controllers;

import app.email.services.MessageEmailService;
import app.email.services.SlotSignificationEmailService;
import app.models.*;
import app.repositories.MatchRepository;
import app.repositories.UserRepository;
import app.services.MatchMessageServiceImpl;
import app.services.SlotServiceImpl;
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
    private final SlotSignificationEmailService slotSignificationEmailService;
    private final SlotServiceImpl slotService;

    @Autowired
    public SlotsSignificationController(SlotsSignificationService slotsSignificationService,
                                        MatchRepository matchRepository,
                                        UserRepository userRepository,
                                        StadiumServiceImpl stadiumService,
                                        MatchMessageServiceImpl matchMessageService,
                                        MessageEmailService messageEmailService,
                                        SlotSignificationEmailService slotSignificationEmailService,
                                        SlotServiceImpl slotService) {
        this.slotsSignificationService = slotsSignificationService;
        this.matchRepository = matchRepository;
        this.userRepository = userRepository;
        this.stadiumService = stadiumService;
        this.matchMessageService = matchMessageService;
        this.messageEmailService = messageEmailService;
        this.slotSignificationEmailService = slotSignificationEmailService;
        this.slotService = slotService;
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
        matchForm = new MatchForm();
        matchForm.setUrlToRedirect(httpServletRequest.getHeader("referer"));

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
            Match match = matchRepository.getOne(matchForm.getId());
            String guestEmail = match.getGuestTeam().getUser().getEmail();
            Slot slot = slotService.findById(slotId);
            Stadium stadium = stadiumService.findById(id);
            String stadiumName = stadium.getName();
            String slotInfo = slot.getFullInfo();
            String homeAndGuest = match.getHomeAndGuest();

            slotsSignificationService.signifySlot(matchForm.getId(), slotId,
                    httpServletRequest.getUserPrincipal().getName());

            slotSignificationEmailService.setSignified(true);
            slotSignificationEmailService.setHomeAndGuest(homeAndGuest);
            slotSignificationEmailService.setSlotInfo(slotInfo);
            slotSignificationEmailService.setStadiumName(stadiumName);
            slotSignificationEmailService.send(guestEmail);

        }
        return "redirect:" + matchForm.getUrlToRedirect();
        //return "redirect:/stadium/" + id;
    }

    @RequestMapping(value = "stadium/{id}/reject/{slotId}", method = RequestMethod.GET)
    public String rejectSlot(@PathVariable Long id, @PathVariable Long slotId, HttpServletRequest httpServletRequest) {
        Slot slot = slotService.findById(slotId);
        Match match = slot.getMatch();
        String homeAndGuest = match.getHomeAndGuest();
        String stadiumName = slot.getStadium().getName();
        String guestEmail = match.getGuestTeam().getUser().getEmail();
        String slotInfo = slot.getFullInfo();
        slotsSignificationService.rejectSlot(slotId, httpServletRequest.getUserPrincipal().getName());

        slotSignificationEmailService.setSignified(false);
        slotSignificationEmailService.setHomeAndGuest(homeAndGuest);
        slotSignificationEmailService.setSlotInfo(slotInfo);
        slotSignificationEmailService.setStadiumName(stadiumName);
        slotSignificationEmailService.send(guestEmail);

        return "redirect:" + httpServletRequest.getHeader("referer");
    }

    @ResponseBody
    @RequestMapping(value = "/save-message/{id}", method = RequestMethod.POST)
    public String saveMessage(@PathVariable("id") Long id, @RequestParam String text,
                              HttpServletRequest httpServletRequest) {
        MatchMessage message = new MatchMessage();
        String email;
        String username = httpServletRequest.getUserPrincipal().getName();
        User user = userRepository.findByEmail(username);
        message.setMessage(text);
        message.setConsidered(false);
        Optional<Match> match = matchRepository.findById(id);
        String homeAndGuest = match.get().getHomeAndGuest();
        if (match.isPresent()) {
            message.setMatch(match.get());
            matchMessageService.save(message);

            messageEmailService.setMessage(message);
            messageEmailService.setMatch(match.get());
            messageEmailService.setHomeAndGuest(homeAndGuest);

            if(user.getRole().equals(Role.adminRole)) {
                messageEmailService.send(match.get().getHomeTeam().getUser().getEmail());
                messageEmailService.send(match.get().getGuestTeam().getUser().getEmail());
            }
            else if (user.getRole().equals(Role.managerRole)) {
                if (message.getConsidered() || match.get().getHomeTeam().getUser().getEmail().equals(username)) {
                    email = message.getMatch().getGuestTeam().getUser().getEmail();
                } else {
                    email = message.getMatch().getHomeTeam().getUser().getEmail();
                }
                messageEmailService.send(email);
            }


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
