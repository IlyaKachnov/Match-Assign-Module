package app.controllers;

import app.models.Stadium;
import app.services.StadiumServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class StadiumController {
    @Autowired
    StadiumServiceImpl stadiumService;

    @RequestMapping(value = "/stadiums", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("stadiums", stadiumService.findAll());

        return "stadiums/index";
    }

    @RequestMapping(value = "/stadiums/create", method = RequestMethod.GET)
    public String showCreateForm(Model model, Stadium stadium) {
        model.addAttribute("stadium", stadium);

        return "stadiums/create";
    }

    @RequestMapping(value = "stadiums/save", method = RequestMethod.POST)
    public String saveStadium(@ModelAttribute Stadium stadium, RedirectAttributes redirectAttributes) {
        if (stadiumService.findByName(stadium.getName()) != null) {
            redirectAttributes.addFlashAttribute("stadium", stadium);
            redirectAttributes.addFlashAttribute("error", true);

            return "redirect:/stadiums/create";
        }

        stadiumService.save(stadium);

        return "redirect:/stadiums";
    }

    @RequestMapping(value = "/stadiums/{id}/edit", method = RequestMethod.GET)
    public String showEditForm(@PathVariable Long id, Model model) {
        Stadium stadium = stadiumService.findById(id);
        model.addAttribute("stadium", stadium);

        return "stadiums/edit";
    }

    @RequestMapping(value = "stadiums/{id}/update", method = RequestMethod.POST)
    public String updateStadium(@PathVariable Long id, @ModelAttribute Stadium stadium, RedirectAttributes redirectAttributes) {
        Stadium stadiumModel = stadiumService.findById(id);
        if (stadiumService.findByName(stadium.getName()) != null && (!stadium.getName().equals(stadiumModel.getName()))) {
            redirectAttributes.addFlashAttribute("stadium", stadium);
            redirectAttributes.addFlashAttribute("error", true);

            return "redirect:/stadiums/" + id + "/edit";
        }
        stadiumModel.setName(stadium.getName());
        stadiumService.save(stadiumModel);

        return "redirect:/stadiums";
    }

    @RequestMapping(value = "stadiums/{id}/delete", method = RequestMethod.POST)
    public String deleteStadium(@PathVariable Long id) {
        stadiumService.delete(id);

        return "redirect:/stadiums";
    }

    @RequestMapping(value = "/all-stadiums", method = RequestMethod.GET)
    public String showStadiumList(Model model) {

        model.addAttribute("stadiums", stadiumService.findAllWithSlots());
        return "stadiums/all-stadiums";
    }

}
