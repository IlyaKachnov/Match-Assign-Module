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

    @RequestMapping(value = "/leagues/{id}/teams/create", method = RequestMethod.GET)
    public String showCreateForm(@PathVariable Long id,  Model model, Team team)
    {
        model.addAttribute("team", team);
        model.addAttribute("league", leagueService.findById(id));

        return "teams/create";
    }

    @RequestMapping(value = "/leagues/{id}/teams/save", method = RequestMethod.POST)
    public String saveTeam(@PathVariable Long id, @ModelAttribute Team team)
    {
        Team teamModel = new Team();
        teamModel.setName(team.getName());
        League league = leagueService.findById(id);
        teamModel.setLeague(league);
        teamService.save(teamModel);

        return "redirect:/leagues/" + id + "/teams" ;
    }

    @RequestMapping(value = "leagues/teams/{teamId}/edit", method = RequestMethod.GET)
    public String showEditForm(@PathVariable("teamId") Long teamId, Model model)
    {
        Team team = teamService.findById(teamId);
        model.addAttribute("team", team);

        return "teams/edit";
    }

    @RequestMapping(value = "leagues/teams/{teamId}/update", method = RequestMethod.POST)
    public String updateTeam(@PathVariable("teamId") Long teamId, @ModelAttribute Team team)
    {
        Team teamModel= teamService.findById(teamId);
        Long leagueId = teamModel.getLeague().getId();
        teamModel.setName(team.getName());

        teamService.save(teamModel);

        return "redirect:/leagues/" + leagueId + "/teams";
    }

    @RequestMapping(value = "leagues/teams/{id}/delete", method = RequestMethod.POST)
    public String deleteUser(@PathVariable("id") Long id, @ModelAttribute Team team)
    {
        Long leagueId = teamService.findById(id).getLeague().getId();
        teamService.delete(id);

        return "redirect:/leagues/" + leagueId + "/teams";
    }
}
