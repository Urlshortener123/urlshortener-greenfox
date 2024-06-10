package com.example.demo.controllers;

import com.example.demo.services.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final EmailService emailService;

    @GetMapping({"/", "/index"})
    public String mainPage() {
        return "index";
    }

    @GetMapping("/secured")
    public String securedPage() {
        return "secured";
    }

    // *** ONLY FOR TESTING --->
    @GetMapping("/email")
    @ResponseStatus(HttpStatus.OK)
    public void sendEmailMessage() throws MessagingException {
        String htmlContent = "<p>It can contain <strong>HTML</strong> content.</p>";
        emailService.sendEmail("[URL Shortener] Please verify your registration!", htmlContent, "admin@test.com");
    }
    // <--- ONLY FOR TESTING ***

}
