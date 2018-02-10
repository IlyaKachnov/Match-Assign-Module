package app.controllers;

import app.models.Slot;
import app.models.Stadium;
import app.services.SlotServiceImpl;
import app.services.StadiumServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

@Controller
public class SlotController {
    @Autowired
    SlotServiceImpl slotService;
    @Resource
    StadiumServiceImpl stadiumService;
    @RequestMapping(value = "/stadiums/{id}/slots", method = RequestMethod.GET)
    public String index(@PathVariable Long id, Model model)
    {
        Stadium stadium =  stadiumService.findById(id);
        model.addAttribute("stadium", stadium);

        return "slots/index";
    }

    @RequestMapping(value = "/stadiums/{id}/slots/create", method = RequestMethod.GET)
    public String showCreateForm(@PathVariable Long id, Model model, Slot slot)
    {
        Stadium stadium =  stadiumService.findById(id);
        model.addAttribute("stadium", stadium);
        model.addAttribute("slot", slot);

        return "slots/create";
    }

    @RequestMapping(value = "/stadiums/{id}/slots/save", method = RequestMethod.POST)
    public String saveEmptySlot(@PathVariable Long id, @ModelAttribute Slot slot)
    {
        Stadium stadium = stadiumService.findById(id);
        Slot newSlot = new Slot();
        newSlot.setEventName(slot.getEventName());
        newSlot.setStartTime(slot.getStartTime());
        newSlot.setEndTime(slot.getEndTime());
        newSlot.setEventDate(slot.getEventDate());
        newSlot.setStadium(stadium);
        slotService.save(newSlot);

        return "redirect:/stadium/" + id + "/slots";
    }


}
