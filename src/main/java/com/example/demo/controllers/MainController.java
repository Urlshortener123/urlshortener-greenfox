package com.example.demo.controllers;

import com.example.demo.utils.UserUtilities;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
@RequiredArgsConstructor
public class MainController {

    @GetMapping({"/", "/index"})
    public String mainPage(Model model) {
        boolean isAuthenticated = UserUtilities.isLoggedIn();
        model.addAttribute("authenticated", isAuthenticated);
        return "index";
    }

    @GetMapping("/secured")
    public String securedPage() {
        return "secured";
    }

}
