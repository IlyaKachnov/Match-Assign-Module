package app.controllers;

import app.models.Match;
import app.models.Slot;
import app.models.Stadium;
import app.services.MatchServiceImpl;
import app.services.SlotServiceImpl;
import app.services.SlotTypeServiceImpl;
import app.services.StadiumServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Calendar;
import java.util.Date;

@Controller
public class SlotController {

    private final SlotServiceImpl slotService;
    private final StadiumServiceImpl stadiumService;
    private final SlotTypeServiceImpl slotTypeService;
    private final MatchServiceImpl matchService;

    @Autowired
    public SlotController(SlotServiceImpl slotService, StadiumServiceImpl stadiumService,
                          SlotTypeServiceImpl slotTypeService, MatchServiceImpl matchService) {
        this.slotService = slotService;
        this.stadiumService = stadiumService;
        this.slotTypeService = slotTypeService;
        this.matchService = matchService;
    }

    @RequestMapping(value = "/stadiums/{id}/slots", method = RequestMethod.GET)
    public String index(@PathVariable Long id, Model model) {
        Stadium stadium = stadiumService.findById(id);
        model.addAttribute("stadium", stadium);

        return "slots/index";
    }

    @RequestMapping(value = "/stadiums/{id}/slots/create", method = RequestMethod.GET)
    public String showCreateForm(@PathVariable Long id, Model model, Slot slot) {
        Stadium stadium = stadiumService.findById(id);
        model.addAttribute("stadium", stadium);
        model.addAttribute("slot", slot);
        model.addAttribute("slotTypes", slotTypeService.findAll());

        return "slots/create";
    }

    @RequestMapping(value = "/stadiums/{id}/slots/save", method = RequestMethod.POST)
    public String saveEmptySlot(@PathVariable Long id, @ModelAttribute Slot slot) {
        Stadium stadium = stadiumService.findById(id);
        Slot newSlot = new Slot();
        newSlot.setStartTime(slot.getStartTime());
        newSlot.setEventDate(slot.getEventDate());
        newSlot.setStadium(stadium);
        newSlot.setSlotType(slot.getSlotType());

        Calendar duration = Calendar.getInstance();
        Calendar time = Calendar.getInstance();

        duration.setTime(slot.getSlotType().getDuration());
        time.setTime(slot.getStartTime());

        int hour = duration.get(Calendar.HOUR);
        int minutes = duration.get(Calendar.MINUTE);

        time.add(Calendar.MINUTE, minutes);
        time.add(Calendar.HOUR, hour);

        newSlot.setEndTime(time.getTime());

        slotService.save(newSlot);

        return "redirect:/stadiums/" + id + "/slots";
    }

    @RequestMapping(value = "/stadiums/{id}/slots/{slotId}/edit", method = RequestMethod.GET)
    public String showEditForm(@PathVariable Long id, @PathVariable Long slotId, Model model) {
        Slot slot = slotService.findById(slotId);
        model.addAttribute("slot", slot);
        model.addAttribute("stadium", stadiumService.findById(id));
        model.addAttribute("slotTypes", slotTypeService.findAll());

        return "slots/edit";
    }

    @RequestMapping(value = "/stadiums/{id}/slots/{slotId}/update", method = RequestMethod.POST)
    public String updateSlot(@PathVariable Long id, @PathVariable("slotId") Long slotId, @ModelAttribute Slot slot) {
        Slot slotModel = slotService.findById(slotId);
        slotModel.setSlotType(slot.getSlotType());
        slotModel.setStartTime(slot.getStartTime());
        slotModel.setEventDate(slot.getEventDate());
        slotModel.setSlotType(slot.getSlotType());

        Calendar duration = Calendar.getInstance();
        Calendar time = Calendar.getInstance();

        duration.setTime(slot.getSlotType().getDuration());
        time.setTime(slot.getStartTime());

        time.add(Calendar.MINUTE, duration.get(Calendar.MINUTE));
        time.add(Calendar.HOUR, duration.get(Calendar.HOUR));

        slotModel.setEndTime(time.getTime());

        slotService.save(slotModel);

        return "redirect:/stadiums/" + id + "/slots";
    }

    @RequestMapping(value = "/stadiums/{id}/slots/{slotId}/delete", method = RequestMethod.POST)
    public String deleteSlot(@PathVariable("id") Long id, @PathVariable("slotId") Long slotId, @ModelAttribute Slot slot) {
        Match match = matchService.findById(slotService.findById(slotId).getMatch().getId());
        match.setSlot(null);
        slotService.delete(slotId);

        return "redirect:/stadiums/" + id + "/slots";
    }

}
