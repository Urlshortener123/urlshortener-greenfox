package com.example.demo.controllers;

import com.example.demo.utils.UserUtilities;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid username or password. Please try again!");
        }
        if (UserUtilities.isLoggedIn()) {
            return "redirect:/index"; // Redirect to main page if already authenticated
        }
        return "login"; // Return login page
    }
}
