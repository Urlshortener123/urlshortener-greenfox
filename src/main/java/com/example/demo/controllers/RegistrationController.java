package com.example.demo.controllers;

import com.example.demo.DTO.CreateUserRequest;
import com.example.demo.models.UserVerificationToken;
import com.example.demo.services.EmailService;
import com.example.demo.services.RegistrationService;
import com.example.demo.services.UserService;
import com.example.demo.utilities.UserUtilities;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;
    private final UserService userService;
    private final EmailService emailService;
    private final UserUtilities userUtilities;

    @GetMapping("/register")
    public String registerForm() {
        //Is the user logged in?
        if (userUtilities.isLoggedIn()) {
            return "redirect:/index";
        }
        return "register";
    }

    @PostMapping("/register")
    public String registerSubmit(CreateUserRequest createUserRequest, Model model) {
        //User registration
        try {
            registrationService.registerUser(createUserRequest);
            model.addAttribute("successMessage", "Registration is successful");
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }
        //Sending email for verification
        try {
            String hashKey = userService.selectVerificationToken(userService.selectUser(createUserRequest.getUsername()));
            emailService.sendEmail("[URL Shortener] Please verify your registration!", createUserRequest.getEmail(), createUserRequest.getUsername(), hashKey);
        } catch (MessagingException e) {
            log.error("Failed to send verification e-mail...", e);
        }
        return "index";
    }

    @GetMapping("/verify")
    public String verifyRegistration(@RequestParam("hash") String hashKey, RedirectAttributes redirectAttributes) {
        UserVerificationToken userVerificationToken = userService.selectVerificationTokenByHash(hashKey);
        if (userVerificationToken != null) {
            userService.validateUser(userVerificationToken);
            //Adding a message as flashAttribute for login page
            redirectAttributes.addFlashAttribute("verificationMessage", "Your registration has been verified, now you are able to log in!");
        }
        return "redirect:/login";
    }

}