package app.controllers;

import app.models.League;
import app.models.Team;
import app.services.LeagueServiceImpl;
import app.services.TeamServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
public class TeamController {

    @Resource
    LeagueServiceImpl leagueService;

    @Autowired
    TeamServiceImpl teamService;

    @RequestMapping(value = "/teams/create", method = RequestMethod.GET)
    public String showCreateForm(Model model, Team team)
    {
        model.addAttribute("team", team);
        model.addAttribute("leagueList", leagueService.findAll());

        return "teams/create";
    }

    @RequestMapping(value = "/teams/save", method = RequestMethod.POST)
    public String saveTeam(@ModelAttribute Team team, Team teamModel)
    {
        teamModel.setName(team.getName());
        Long leagueId = team.getLeague().getId();
        League selectedLeague = leagueService.findById(leagueId);
        teamModel.setLeague(selectedLeague);
        teamService.save(teamModel);

        return "redirect:/leagues/" + leagueId + "/teams" ;
    }

    @RequestMapping(value = "/teams/{id}/edit", method = RequestMethod.GET)
    public String showEditForm(@PathVariable("id") Long id, Model model)
    {
        Team team = teamService.findById(id);
        model.addAttribute("team", team);
        model.addAttribute("leagueList", leagueService.findAll());

        return "teams/edit";
    }

    @RequestMapping(value = "/teams/{id}/update", method = RequestMethod.POST)
    public String updateTeam(@PathVariable("id") Long id, @ModelAttribute Team team)
    {
        Team teamModel= teamService.findById(id);
        Long leagueId = teamModel.getLeague().getId();
        teamModel.setName(team.getName());
        teamModel.setLeague(team.getLeague());
        teamService.save(teamModel);

        return "redirect:/leagues/" + leagueId + "/teams";
    }

    @RequestMapping(value = "/teams/{id}/delete", method = RequestMethod.POST)
    public String deleteUser(@PathVariable("id") Long id, @ModelAttribute Team team)
    {
        Long leagueId = teamService.findById(id).getLeague().getId();
        teamService.delete(id);

        return "redirect:/leagues/" + leagueId + "/teams";
    }
}
