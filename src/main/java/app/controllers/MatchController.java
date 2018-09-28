package app.controllers;

import app.models.Match;
import app.models.Slot;
import app.models.Stadium;
import app.models.Team;
import app.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class MatchController {

    private final MatchService matchService;
    private final TeamServiceImpl teamService;
    private final SlotServiceImpl slotService;
    private final LeagueServiceImpl leagueService;
    private final TourServiceImpl tourService;
    private final StadiumServiceImpl stadiumService;
    private final SlotsSignificationService slotsSignificationService;

    @Autowired
    public MatchController(MatchService matchService, TeamServiceImpl teamService,
                           SlotServiceImpl slotService, LeagueServiceImpl leagueService, TourServiceImpl tourService,
                           StadiumServiceImpl stadiumService, SlotsSignificationService slotsSignificationService) {
        this.matchService = matchService;
        this.teamService = teamService;
        this.slotService = slotService;
        this.leagueService = leagueService;
        this.tourService = tourService;
        this.stadiumService = stadiumService;
        this.slotsSignificationService = slotsSignificationService;
    }

    @RequestMapping(value = "/matches", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("matches", matchService.findAll());
        model.addAttribute("teams", teamService.findAll());

        return "matches/index";
    }

    @RequestMapping(value = "/matches/create", method = RequestMethod.GET)
    public String showCreateForm(Model model, Match match) {
        model.addAttribute("teams", teamService.findAllOrderByName());
        model.addAttribute("match", match);
        model.addAttribute("leagueList", leagueService.findAll());

        return "matches/create";
    }

    @RequestMapping(value = "/matches/save", method = RequestMethod.POST)
    public String saveMatch(@ModelAttribute Match match) {
        matchService.save(match);

        return "redirect:/matches";

    }

    @RequestMapping(value = "/matches/{id}/edit", method = RequestMethod.GET)
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Match match = matchService.findById(id);
        List<Team> teams = match.getTour().getLeague().getTeams();
        model.addAttribute("teams", teams);
        model.addAttribute("match", match);
        model.addAttribute("leagueList", leagueService.findAll());

        return "matches/edit";
    }

    @RequestMapping(value = "/matches/{id}/update", method = RequestMethod.POST)
    public String updateMatch(@PathVariable Long id, @ModelAttribute Match match) {
        Match matchModel = matchService.findById(id);
        Team homeTeam = match.getHomeTeam();
        Team guestTeam = match.getGuestTeam();
        matchModel.setHomeTeam(homeTeam);
        matchModel.setGuestTeam(guestTeam);
        matchModel.setTour(match.getTour());
        matchModel.setDelayed(match.getDelayed());

        matchService.save(matchModel);

        return "redirect:/matches";

    }

    @RequestMapping(value = "/matches/{id}/delete", method = RequestMethod.POST)
    public String deleteMatch(@PathVariable("id") Long id) {
        if(matchService.findById(id).getSlot()!= null){
            Slot slot = slotService.findById(matchService.findById(id).getSlot().getId());
            slot.setMatch(null);
            slotService.save(slot);
        }
        matchService.delete(id);

        return "redirect:/matches";
    }

    @RequestMapping(value = "/all-matches", method = RequestMethod.GET)
    public String showMatchList(Model model, HttpServletRequest httpServletRequest) {
        model.addAttribute("matches", matchService.generateJSON(httpServletRequest.getUserPrincipal().getName()));
        model.addAttribute("tours", tourService.generateTourFilterJSON());
        model.addAttribute("leagues", leagueService.generateLeagueFilterJSON());
        model.addAttribute("leagueList", leagueService.findWithMatches());
        model.addAttribute("tourList", tourService.findWithMatches());

        return "matches/all-matches";
    }

    @ResponseBody
    @RequestMapping(value = "/matches/{id}/change", method = RequestMethod.GET)
    public String onChangeTeams(@PathVariable("id") Long id) {

        return leagueService.getTeamsJSON(id);
    }

    @GetMapping(value = "matches/{id}/signify")
    public String showFastSignificationForm(@PathVariable("id") Long id, Model model){
        Match match = matchService.findById(id);
        List<Stadium> stadiums = stadiumService.findAllWithSlots();
        if(match == null) {
            return "error/404";
        }
        model.addAttribute("match", match);
        model.addAttribute("stadiums", stadiums);

        return "matches/fast-signify";

    }

    @PostMapping(value = "matches/{matchId}/signify")
    public String signifySlot(@PathVariable Long matchId, @ModelAttribute Match match,
                              HttpServletRequest httpServletRequest){
        String userEmail = httpServletRequest.getUserPrincipal().getName();
        slotsSignificationService.signifySlot(matchId, match.getSlot().getId(), userEmail);
        return "redirect:/matches";
    }

    @GetMapping(value = "matches/{matchId}/reject")
    public String rejectSlot(@PathVariable Long matchId, HttpServletRequest httpServletRequest) {
        String userEmail = httpServletRequest.getUserPrincipal().getName();
        Match match = matchService.findById(matchId);
        slotsSignificationService.rejectSlot(match.getSlot().getId(), userEmail);
        return "redirect:/matches";
    }
}
