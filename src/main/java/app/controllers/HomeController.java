package app.controllers;

import app.dto.MatchDTO;
import app.dto.StadiumSlotsDTO;
import app.models.User;
import app.services.MatchService;
import app.services.StadiumService;
import app.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    private final StadiumService stadiumService;
    private final UserService userService;
    private final MatchService matchService;

    public HomeController(StadiumService stadiumService, UserService userService,
                          MatchService matchService) {
        this.stadiumService = stadiumService;
        this.userService = userService;
        this.matchService = matchService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model, Principal principal) {

        User user = userService.findByEmail(principal.getName());
        List<StadiumSlotsDTO> stadiumsWithActualSlots = stadiumService.findAllWithSlotsByUser(user);
        List<MatchDTO> matchDTOList = matchService.getMatchDTOList(principal.getName());
        stadiumsWithActualSlots.forEach(stadiumSlotsDTO -> stadiumSlotsDTO.getSlotDTO().forEach(slotDTO -> {
            if ((slotDTO.getUrl() == null || slotDTO.getUrl().isEmpty())
                    && slotDTO.getSlot().getMatch() != null) {
                Optional<MatchDTO> matchDTO = matchDTOList.stream().filter(currMatchDTO ->
                        currMatchDTO.getId().equals(slotDTO.getSlot().getMatch().getId())).findFirst();
                matchDTO.ifPresent(matchDTO1 -> slotDTO.setUrl(matchDTO1.getMessage()));
            }
        }));

        model.addAttribute("stadiums", stadiumsWithActualSlots);
        return "index";
    }
}
