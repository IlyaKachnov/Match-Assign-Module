package app.controllers;

import app.models.Slot;
import app.services.SlotServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SlotController {
    @Autowired
    SlotServiceImpl slotService;

    @RequestMapping(value = "/slots", method = RequestMethod.GET)
    public String index(Model model)
    {
        model.addAttribute("slots", slotService.findAll());

        return "slots/index";
    }

    @RequestMapping(value = "/slots/create", method = RequestMethod.GET)
    public String showCreateForm(Model model, Slot slot)
    {
        model.addAttribute("slot", slot);

        return "slots/create";
    }


}
