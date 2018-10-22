package app.controllers;

import app.models.*;
import app.services.LeagueServiceImpl;
import app.services.TeamServiceImpl;
import app.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class TeamController {

    private LeagueServiceImpl leagueService;
    private TeamServiceImpl teamService;
    private UserServiceImpl userService;

    @Autowired
    public TeamController(LeagueServiceImpl leagueService, TeamServiceImpl teamService, UserServiceImpl userService) {
        this.leagueService = leagueService;
        this.teamService = teamService;
        this.userService = userService;
    }

    @RequestMapping(value = "/leagues/{id}/teams/create", method = RequestMethod.GET)
    public String showCreateForm(@PathVariable Long id, Model model, Team team) {
        model.addAttribute("team", team);
        model.addAttribute("league", leagueService.findById(id));

        return "teams/create";
    }

    @RequestMapping(value = "/leagues/{id}/teams/save", method = RequestMethod.POST)
    public String saveTeam(@PathVariable Long id, @ModelAttribute Team team, RedirectAttributes redirectAttributes) {
        if (teamService.findByName(team.getName()) != null) {
            redirectAttributes.addFlashAttribute("team", team);
            redirectAttributes.addFlashAttribute("error", true);

            return "redirect:/leagues/" + id + "/teams/create";
        }

        Team teamModel = new Team();
        teamModel.setName(team.getName());
        League league = leagueService.findById(id);
        teamModel.setLeague(league);

        teamService.save(teamModel);

        return "redirect:/leagues/" + id + "/teams";
    }

    @RequestMapping(value = "leagues/teams/{teamId}/edit", method = RequestMethod.GET)
    public String showEditForm(@PathVariable("teamId") Long teamId, Model model) {
        Team team = teamService.findById(teamId);
        model.addAttribute("team", team);

        return "teams/edit";
    }

    @RequestMapping(value = "leagues/teams/{teamId}/update", method = RequestMethod.POST)
    public String updateTeam(@PathVariable("teamId") Long teamId, @ModelAttribute Team team, RedirectAttributes redirectAttributes) {
        Team teamModel = teamService.findById(teamId);

        if (teamService.findByName(team.getName()) != null && (!team.getName().equals(teamModel.getName()))) {
            redirectAttributes.addFlashAttribute("team", team);
            redirectAttributes.addFlashAttribute("error", true);

            return "redirect:/leagues/teams/" + teamId + "/edit";
        }

        Long leagueId = teamModel.getLeague().getId();
        teamModel.setName(team.getName());

        teamService.save(teamModel);

        return "redirect:/leagues/" + leagueId + "/teams";
    }

    @RequestMapping(value = "leagues/teams/{id}/delete", method = RequestMethod.GET)
    public String deleteTeam(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        Long leagueId = teamService.findById(id).getLeague().getId();
        Team team = teamService.findById(id);
        List<Match> matchesAsHome = team.getMatchesAsHome();
        List<Match> matchesAsGuest = team.getMatchAsGuest();
        if(!matchesAsHome.isEmpty() || !matchesAsGuest.isEmpty()) {
            String error = "Невозможно удалить команду, так как она имеет матчи!";
            redirectAttributes.addFlashAttribute("error", error);
            return "redirect:/leagues/" + leagueId + "/teams";
        }
        teamService.delete(id);

        return "redirect:/leagues/" + leagueId + "/teams";
    }

    @GetMapping(value = "my-teams")
    public String getCurrentUserTeams(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        if (user.getRole().equals(Role.adminRole)) {
            return "error/404";
        }

        model.addAttribute("teams", user.getTeamList());
        return "teams/my_teams";
    }
}
