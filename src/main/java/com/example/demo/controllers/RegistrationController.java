package com.example.demo.controllers;

import com.example.demo.DTO.CreateUserRequest;
import com.example.demo.models.UserVerificationToken;
import com.example.demo.services.EmailService;
import com.example.demo.services.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private final UserService userService;
    private final EmailService emailService;

    private boolean isLoggedIn() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.isAuthenticated();
    }

    @GetMapping("/register")
    public String registerForm() {
        //Is the user logged in?
        if (isLoggedIn()) {
            return "redirect:/index";
        }
        return "register";
    }

    @PostMapping("/register")
    public String registerSubmit(CreateUserRequest createUserRequest, Model model) {
        //Does the user already exist?
        String userName = createUserRequest.getUsername();
        if (userService.selectUser(userName) != null) {
            model.addAttribute("errorMessage", "User already exists");
            return "register";
        }

        //Check if email was already used
        String userEmail = createUserRequest.getEmail();
        if (userEmail != null && userService.emailIsVerified(userEmail)) {
            model.addAttribute("errorMessage", "E-mail was already verified by another user");
            return "register";
        }

        //Successful user registration
        userService.addUser(createUserRequest);
        model.addAttribute("successMessage", "Registration is successful");

        //Sending email for verification
        try {
            String hashKey = userService.selectVerificationToken(userService.selectUser(userName));
            emailService.sendEmail("[URL Shortener] Please verify your registration!", userEmail, userName, hashKey);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return "index";
    }

    @GetMapping("/verify")
    public String verifyRegistration(@RequestParam("hash") String hashKey, RedirectAttributes redirectAttributes) {
        UserVerificationToken userVerificationToken = userService.selectVerificationTokenByHash(hashKey);
        if (userVerificationToken != null) {
            userVerificationToken.getUser().setEmailVerified(true);
            userService.deleteVerificationToken(userVerificationToken);
            //Adding a message as flashAttribute for login page
            redirectAttributes.addFlashAttribute("verificationMessage", "Your registration has been verified, now you are able to log in!");
        }
        return "redirect:/login";
    }

}