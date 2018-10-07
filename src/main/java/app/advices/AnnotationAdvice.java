package app.advices;

import app.dto.NotificationDTO;
import app.models.Role;
import app.models.User;
import app.services.SlotsSignificationService;
import app.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;
import java.util.List;

@ControllerAdvice(annotations = Controller.class)
public class AnnotationAdvice {

    private UserServiceImpl userService;
    private SlotsSignificationService slotsSignificationService;

    @Autowired
    public AnnotationAdvice(UserServiceImpl userService, SlotsSignificationService slotsSignificationService) {
        this.userService = userService;
        this.slotsSignificationService = slotsSignificationService;
    }

//    @ModelAttribute("futureSessions")
//    public List<SlotSignificationTime> getFutureSessions(Principal principal) {
//        User user  = userService.findByEmail(principal.getName());
//        if(user.getRole().equals(Role.adminRole)) {
//            return slotsSignificationService.getFutureSessions();
//        }
//
//        return slotsSignificationService.getFutureSessions(user);
//    }
//
//    @ModelAttribute("actualSessions")
//    public List<SlotSignificationTime> getActualSessions(Principal principal) {
//        User user  = userService.findByEmail(principal.getName());
//        if(user.getRole().equals(Role.adminRole)) {
//            return slotsSignificationService.getActualSessions();
//        }
//
//        return slotsSignificationService.getActualSessions(user);
//    }

    @ModelAttribute("notifications")
    public List<NotificationDTO> getNotifications(Principal principal) {
        User user = userService.findByEmail(principal.getName());
        if (user.getRole().equals(Role.adminRole)) {
            return slotsSignificationService.getNotifications();
        }


        return slotsSignificationService.getNotifications(user);
    }
}
