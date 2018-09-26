package app.controllers;

import app.email.services.ResetPasswordEmailService;
import app.models.User;
import app.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class PasswordResetController {
    private UserServiceImpl userService;
    private ResetPasswordEmailService resetPasswordEmailService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public PasswordResetController(UserServiceImpl userService, ResetPasswordEmailService resetPasswordEmailService,
                                   BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.resetPasswordEmailService = resetPasswordEmailService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping(value = "/forgot")
    public ResponseEntity processForgotPasswordForm(@RequestParam("email") String email, HttpServletRequest request) {
        User user = userService.findByEmail(email);

        if(user == null) {
            return  new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        //TODO: without port
        String rootUrl = request.getScheme() + "://" + request.getServerName() + ":8080";

        String token = UUID.randomUUID().toString();
        String resetUrl = rootUrl + "/reset?token=" + token;
        user.setResetToken(token);
        userService.save(user);

        resetPasswordEmailService.setResetUrl(resetUrl);
        resetPasswordEmailService.send(email);

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/reset")
    public String showPasswordResetForm(Model model, @RequestParam("token") String token) {
        User user = userService.findByResetToken(token);
        if(user == null) {
            return "error/404";
        }
        model.addAttribute("token", token);

        return "password/reset_password";
    }

    @PostMapping(value = "/reset")
    public ResponseEntity processResetPasswordForm(@RequestParam("token") String token,
                                                   @RequestParam("password") String password) {
        User user = userService.findByResetToken(token);
        if (user == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setResetToken(null);
        userService.save(user);
        return new ResponseEntity(HttpStatus.OK);
    }

}
