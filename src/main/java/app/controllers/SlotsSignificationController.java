package app.controllers;

import app.models.*;
import app.repositories.MatchRepository;
import app.repositories.SlotRepository;
import app.repositories.StadiumRepository;
import app.repositories.UserRepository;
import app.services.SlotsSignificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SlotsSignificationController {

    private final SlotsSignificationService slotsSignificationService;
    private final MatchRepository matchRepository;
    private final UserRepository userRepository;

    @Autowired
    public SlotsSignificationController(SlotsSignificationService slotsSignificationService,
                                        MatchRepository matchRepository,
                                        UserRepository userRepository) {
        this.slotsSignificationService = slotsSignificationService;
        this.matchRepository = matchRepository;
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "stadium/{id}", method = RequestMethod.GET)
    public String showStadiumSlots(@PathVariable Long id, Model model,
                                   HttpServletRequest httpServletRequest) {
        model.addAttribute("slots",
                slotsSignificationService.generateSlotsJSON(id, httpServletRequest.getUserPrincipal().getName()));
        return "slots_signification/index";
    }

    @RequestMapping(value = "/stadium/{id}/signify/{slotId}", method = RequestMethod.GET)
    public String showCreateForm(@PathVariable Long id, @PathVariable Long slotId,
                                 Model model, HttpServletRequest httpServletRequest,
                                 MatchForm matchForm) {
        User currUser = userRepository.findByEmail(httpServletRequest.getUserPrincipal().getName());
        List<Team> userTeams = currUser.getTeamList();
        List<Match> actualMatches = new ArrayList<>();
        userTeams.forEach(team -> {
            actualMatches.addAll(matchRepository.findByHomeTeam(team));
        });
        model.addAttribute("home_teams", userTeams);
        model.addAttribute("matches", actualMatches);
        model.addAttribute("id", id);
        model.addAttribute("slotId", slotId);
        model.addAttribute("matchForm", matchForm);
        return "slots_signification/signify";
    }

    @RequestMapping(value = "stadium/{id}/signify/{slotId}", method = RequestMethod.POST)
    public String signifySlot(@PathVariable Long id, @PathVariable Long slotId,
                              @ModelAttribute MatchForm matchForm) {
        slotsSignificationService.signifySlot(matchForm.getId(), slotId);
        return "redirect:/stadium/" + id;
    }

    @RequestMapping(value = "stadium/{id}/reject/{slotId}", method = RequestMethod.GET)
    public String rejectSlot(@PathVariable Long id, @PathVariable Long slotId) {
        slotsSignificationService.rejectSlot(slotId);
        return "redirect:/stadium/" + id;
    }
}
