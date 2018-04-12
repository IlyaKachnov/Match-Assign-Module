package app.controllers;

import app.models.Match;
import app.models.Slot;
import app.models.Team;
import app.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

@Controller
public class MatchController {

    private final MatchServiceImpl matchService;
    private final TeamServiceImpl teamService;
    private final SlotServiceImpl slotService;
    private final LeagueServiceImpl leagueService;
    @Autowired
    public MatchController(MatchServiceImpl matchService, TeamServiceImpl teamService, SlotServiceImpl slotService, LeagueServiceImpl leagueService){
        this.matchService = matchService;
        this.teamService = teamService;
        this.slotService = slotService;
        this.leagueService = leagueService;
    }

    @RequestMapping(value = "/matches", method = RequestMethod.GET)
    public String index(Model model)
    {
        model.addAttribute("matches", matchService.findAll());
        model.addAttribute("teams", teamService.findAll());

        return "matches/index";
    }

    @RequestMapping(value = "/matches/create", method = RequestMethod.GET)
    public String showCreateForm(Model model, Match match)
    {
        model.addAttribute("teams", teamService.findAll());
        model.addAttribute("match", match);
        model.addAttribute("leagueList", leagueService.findAll());

        return "matches/create";
    }

    @RequestMapping(value = "/matches/save", method = RequestMethod.POST)
    public String saveMatch(@ModelAttribute Match match)
    {
        matchService.save(match);

        return "redirect:/matches";

    }

    @RequestMapping(value = "/matches/{id}/edit", method = RequestMethod.GET)
    public String showEditForm(@PathVariable("id") Long id, Model model)
    {
        Match match = matchService.findById(id);
        model.addAttribute("teams", teamService.findAll());
        model.addAttribute("match", match);
        model.addAttribute("leagueList", leagueService.findAll());

        return "matches/edit";
    }

    @RequestMapping(value = "/matches/{id}/update", method = RequestMethod.POST)
    public String updateMatch(@PathVariable Long id, @ModelAttribute Match match)
    {
        Match matchModel = matchService.findById(id);
        Team homeTeam = match.getHomeTeam();
        Team guestTeam = match.getGuestTeam();
        matchModel.setHomeTeam(homeTeam);
        matchModel.setGuestTeam(guestTeam);
        matchModel.setMatchDate(match.getMatchDate());

        matchService.save(matchModel);

        return "redirect:/matches";

    }

    @RequestMapping(value = "/matches/{id}/delete", method = RequestMethod.POST)
    public String deleteMatch(@PathVariable("id") Long id)
    {
        Slot slot = slotService.findById(matchService.findById(id).getSlot().getId());
        slot.setMatch(null);
        slotService.save(slot);
        matchService.delete(id);

        return "redirect:/matches";
    }

    @RequestMapping(value = "/all-matches", method = RequestMethod.GET)
    public String showMatchList(Model model)
    {
        model.addAttribute("matches", matchService.findAll());

        return "matches/all-matches";
    }

}
