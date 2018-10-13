package app.controllers;

import app.models.League;
import app.services.LeagueServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
public class LeagueController {
    @Autowired
    LeagueServiceImpl leagueService;

    @RequestMapping(value = "/leagues", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("leagues", leagueService.findAll());

        return "leagues/index";
    }

    @RequestMapping(value = "/leagues/create", method = RequestMethod.GET)
    public String showCreateForm(Model model, League league) {


        model.addAttribute("league", league);

        return "leagues/create";

    }

    @RequestMapping(value = "/leagues/save", method = RequestMethod.POST)
    public String saveLeague(@ModelAttribute League league, RedirectAttributes redirectAttributes) {
        if (leagueService.findByName(league.getName()) != null) {
            redirectAttributes.addFlashAttribute("league", league);
            redirectAttributes.addFlashAttribute("error", true);

            return "redirect:/leagues/create";
        }

        leagueService.save(league);

        return "redirect:/leagues";
    }

    @RequestMapping(value = "/leagues/{id}/teams", method = RequestMethod.GET)
    public String showTeams(@PathVariable("id") Long id, Model model) {
        League league = leagueService.findById(id);

        model.addAttribute("league", league);

        return "teams/index";
    }

    @RequestMapping(value = "/leagues/{id}/edit", method = RequestMethod.GET)
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        League league = leagueService.findById(id);
        model.addAttribute("league", league);

        return "leagues/edit";
    }

    @RequestMapping(value = "/leagues/{id}/update", method = RequestMethod.POST)
    public String updateLeague(@PathVariable("id") Long id, @ModelAttribute League league, RedirectAttributes redirectAttributes) {
        League leagueModel = leagueService.findById(id);
        if (leagueService.findByName(league.getName()) != null && (!league.getName().equals(leagueModel.getName()))) {
            redirectAttributes.addFlashAttribute("league", league);
            redirectAttributes.addFlashAttribute("error", true);

            return "redirect:/leagues/" + id + "/edit";
        }
        leagueModel.setName(league.getName());
        leagueService.save(leagueModel);

        return "redirect:/leagues";
    }

    @RequestMapping(value = "/leagues/{id}/delete", method = RequestMethod.GET)
    public String deleteLeague(@PathVariable("id") Long id, @ModelAttribute League league) {
        leagueService.delete(id);

        return "redirect:/leagues";
    }
}
