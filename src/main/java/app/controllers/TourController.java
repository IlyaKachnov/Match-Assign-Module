package app.controllers;

import app.models.Team;
import app.models.Tour;
import app.services.LeagueServiceImpl;
import app.services.TourServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TourController {
    private final TourServiceImpl tourService;
    private final LeagueServiceImpl leagueService;

    @Autowired
    public TourController(TourServiceImpl tourService, LeagueServiceImpl leagueService) {
        this.tourService = tourService;
        this.leagueService = leagueService;
    }

    @RequestMapping(value = "/tours", method = RequestMethod.GET)
    public String index(Model model) {

        model.addAttribute("tours", tourService.findAll());

        return "tours/index";
    }

    @RequestMapping(value = "/tours/create", method = RequestMethod.GET)
    public String showCreateForm(Model model, Tour tourForm) {

        model.addAttribute("tourForm", tourForm);
        model.addAttribute("leagueList", leagueService.findAll());

        return "tours/create";
    }

    @RequestMapping(value = "/tours/save", method = RequestMethod.POST)
    public String saveTour(@ModelAttribute Tour tourForm, RedirectAttributes redirectAttributes) {

//        if (tourService.findByName(tourForm.getName()) != null) {
//            redirectAttributes.addFlashAttribute("tourForm", tourForm);
//            redirectAttributes.addFlashAttribute("error", true);
//
//            return "redirect:/tours/create";
//        }
        if (tourForm.getEndDate().before(tourForm.getStartDate())) {
            redirectAttributes.addFlashAttribute("tourForm", tourForm);
            redirectAttributes.addFlashAttribute("error", true);
            return "redirect:/tours/create";
        }


        tourService.save(tourForm);

        return "redirect:/tours";
    }

    @RequestMapping(value = "tours/{id}/edit", method = RequestMethod.GET)
    public String showEditForm(@PathVariable("id") Long id, Model model) {

        Tour tourForm = tourService.findById(id);
        model.addAttribute("tourForm", tourForm);
        model.addAttribute("leagueList", leagueService.findAll());

        return "tours/edit";

    }

    @RequestMapping(value = "tours/{id}/update", method = RequestMethod.POST)
    public String updateTour(@PathVariable("id") Long id, @ModelAttribute Tour tourForm, RedirectAttributes redirectAttributes) {

        Tour tour = tourService.findById(id);

//        if (tourService.findByName(tourForm.getName()) != null && (!tourForm.getName().equals(tour.getName()))) {
//            redirectAttributes.addFlashAttribute("tourForm", tourForm);
//            redirectAttributes.addFlashAttribute("error", true);
//
//            return "redirect:/tours/" + id + "/edit";
//        }

        if (tourForm.getEndDate().before(tourForm.getStartDate())) {
            redirectAttributes.addFlashAttribute("tourForm", tourForm);
            redirectAttributes.addFlashAttribute("error", true);
            return "redirect:/tours/" + id + "/edit";

        }

        tour.setName(tourForm.getName());
        tour.setStartDate(tourForm.getStartDate());
        tour.setEndDate(tourForm.getEndDate());
        tour.setLeague(tourForm.getLeague());

        tourService.save(tour);

        return "redirect:/tours";
    }

    @RequestMapping(value = "/tours/{id}/delete", method = RequestMethod.GET)
    public String deleteTour(@PathVariable("id") Long id) {
        tourService.delete(id);

        return "redirect:/tours";
    }

}
