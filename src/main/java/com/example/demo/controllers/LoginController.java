package com.example.demo.controllers;

import com.example.demo.utilities.UserUtilities;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class LoginController {
    private final UserUtilities userUtilities;
    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid username or password. Please try again!");
        }
        if (userUtilities.isLoggedIn()) {
            return "redirect:/index"; // Redirect to main page if already authenticated
        }
        return "login"; // Return login page
    }
}
