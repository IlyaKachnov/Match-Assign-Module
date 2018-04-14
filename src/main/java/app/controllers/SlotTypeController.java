package app.controllers;

import app.models.SlotType;
import app.repositories.SlotTypeRepository;
import app.services.SlotTypeServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SlotTypeController {

    private final SlotTypeServiceImpl slotTypeService;

    public SlotTypeController(SlotTypeServiceImpl slotTypeService) {
        this.slotTypeService = slotTypeService;
    }

    @RequestMapping(value = "/slot-types", method = RequestMethod.GET)
    public String index(Model model) {

        model.addAttribute("slotTypes", slotTypeService.findAll());

        return "slot_types/index";
    }

    @RequestMapping(value = "/slot-types/create", method = RequestMethod.GET)
    public String showCreateForm(Model model, SlotType slotType) {

        model.addAttribute("slotType", slotType);

        return "slot_types/create";
    }

    @RequestMapping(value = "/slot-types/save", method = RequestMethod.POST)
    public String saveSlotType(@ModelAttribute SlotType slotType, RedirectAttributes redirectAttributes) {

        if (slotTypeService.findExisted(slotType.getTypeName(), slotType.getDuration())) {
            redirectAttributes.addFlashAttribute("slotType", slotType);
            redirectAttributes.addFlashAttribute("error", true);

            return "redirect:/slot-types/create";
        }

        slotTypeService.save(slotType);

        return "redirect:/slot-types";
    }

    @RequestMapping(value = "/slot-types/{id}/edit", method = RequestMethod.GET)
    public String showEditForm(@PathVariable("id") Long id, Model model) {

        SlotType type = slotTypeService.findById(id);
        model.addAttribute("slotType", type);

        return "slot_types/edit";
    }

    @RequestMapping(value = "/slot-types/{id}/update", method = RequestMethod.POST)
    public String updateSlotType(@PathVariable Long id, @ModelAttribute SlotType slotType, RedirectAttributes redirectAttributes) {

        SlotType type = slotTypeService.findById(id);

        if (slotTypeService.findExisted(slotType.getTypeName(), slotType.getDuration()) &&
                !((slotType.getDuration().equals(type.getDuration())) &&
                        (slotType.getTypeName().equals(type.getTypeName())))) {
            redirectAttributes.addFlashAttribute("slotType", slotType);
            redirectAttributes.addFlashAttribute("error", true);

            return "redirect:/slot-types/{id}/edit";
        }

        type.setDuration(slotType.getDuration());
        type.setTypeName(slotType.getTypeName());
        type.setSignifiable(slotType.getSignifiable());
        slotTypeService.save(type);

        return "redirect:/slot-types";
    }

    @RequestMapping(value = "/slot-types/{id}/delete", method = RequestMethod.POST)
    public String deleteSlotType(@PathVariable Long id) {

        slotTypeService.delete(id);

        return "redirect:/slot-types";
    }

}
