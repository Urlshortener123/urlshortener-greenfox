package com.example.demo.controllers;

import com.example.demo.utilities.UserUtilities;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final UserUtilities userUtilities;

    @GetMapping({"/", "/index"})
    public String mainPage(Model model) {
        boolean isAuthenticated = userUtilities.isLoggedIn();
        model.addAttribute("authenticated", isAuthenticated);
        return "index";
    }

    @GetMapping("/secured")
    public String securedPage() {
        return "secured";
    }

}
