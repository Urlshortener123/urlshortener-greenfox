package com.example.demo.controllers;

import com.example.demo.DTO.CreateUserRequest;
import com.example.demo.models.UserVerificationToken;
import com.example.demo.services.RegistrationService;
import com.example.demo.services.UserService;
import com.example.demo.utilities.UserUtilities;
import com.example.demo.services.VerificationEmailService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    private final VerificationEmailService verificationEmailService;
    private final UserUtilities userUtilities;

    @GetMapping("/register")
    public Object registerForm(Model model) {
        //Is the user logged in?
        if (userUtilities.isLoggedIn()) {
            return "redirect:/index";
        }
        model.addAttribute("createUserRequest", new CreateUserRequest());
        return "register";
    }

    @PostMapping("/register")
    public String registerSubmit(@Valid CreateUserRequest createUserRequest,
                                 BindingResult bindingResult,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        //Data input validation - show errors if inputs are not valid
        if (bindingResult.hasErrors()) {
            return "register";
        }
        //User registration
        try {
            registrationService.registerUser(createUserRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Please check your mailbox to verify your registration!");
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }
        //Sending email for verification
        try {
            String hashKey = userService.selectVerificationToken(userService.selectUser(createUserRequest.getUsername()));
            verificationEmailService.sendVerificationEmail("[URL Shortener] Please verify your registration!", createUserRequest.getEmail(), createUserRequest.getUsername(), hashKey);
        } catch (MessagingException e) {
            log.error("Failed to send verification e-mail...", e);
        }
        return "redirect:/register";
    }

    @GetMapping("/verify")
    public String verifyRegistration(@RequestParam("hash") @NotEmpty (message = "Hash key is missing!") String hashKey, RedirectAttributes redirectAttributes) {
        UserVerificationToken userVerificationToken = userService.selectVerificationTokenByHash(hashKey);
        if (userVerificationToken != null) {
            userService.validateUser(userVerificationToken);
            //Adding a message as flashAttribute for login page
            redirectAttributes.addFlashAttribute("verificationMessage", "Your registration has been verified, now you are able to log in!");
        }
        return "redirect:/login";
    }
}