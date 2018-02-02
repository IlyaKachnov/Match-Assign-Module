package app.controllers;

import app.models.League;
import app.services.LeagueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LeagueController {
    @Autowired
    LeagueService leagueService;
    @RequestMapping(value = "/leagues", method = RequestMethod.GET)
    public String index(Model model)
    {
        model.addAttribute("leagues", leagueService.findAll());

        return "leagues/index";
    }

    @RequestMapping(value = "/leagues/create", method = RequestMethod.GET)
    public String showCreateForm(Model model, League league) {

        model.addAttribute("league", league);

        return "leagues/create";

    }

    @RequestMapping(value = "/leagues/save", method = RequestMethod.POST)
    public String saveLeague(@ModelAttribute League league, League leagueModel)
    {
        leagueModel.setName(league.getName());
        leagueService.save(leagueModel);

        return "redirect:/leagues";
    }

    @RequestMapping(value = "/leagues/{id}/teams", method = RequestMethod.GET)
    public String index(@PathVariable("id") Long id, Model model)
    {
        League league = leagueService.findById(id);

        model.addAttribute("league", league);

        return "teams/index";
    }

    @RequestMapping(value = "/leagues/{id}/edit", method = RequestMethod.GET)
    public String showEditForm(@PathVariable("id") Long id, Model model)
    {
        League league = leagueService.findById(id);
        model.addAttribute("league", league);

        return "leagues/edit";
    }

    @RequestMapping(value = "/leagues/{id}/update", method = RequestMethod.POST)
    public String updateUser(@PathVariable("id") Long id, @ModelAttribute League league)
    {
        League leagueModel = leagueService.findById(id);
        leagueModel.setName(league.getName());
        leagueService.save(leagueModel);

        return "redirect:/leagues";
    }

    @RequestMapping(value = "/leagues/{id}/delete", method = RequestMethod.POST)
    public String deleteUser(@PathVariable("id") Long id, @ModelAttribute League league)
    {
        leagueService.delete(id);

        return "redirect:/leagues";
    }
}
