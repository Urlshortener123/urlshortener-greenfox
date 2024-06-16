package com.example.demo.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;

@Controller
@RequiredArgsConstructor
public class MainController {

    @GetMapping({"/", "/index"})
    public String mainPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = auth != null && auth.isAuthenticated();
        model.addAttribute("authenticated", isAuthenticated);
        return "index";
    }

    @GetMapping("/secured")
    public String securedPage() {
        return "secured";
    }

}
