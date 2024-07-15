package com.example.demo.controllers;

import com.example.demo.DTO.UrlRequest;
import com.example.demo.utilities.UserUtilities;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final UserUtilities userUtilities;

    @GetMapping({"/", "/index"})
    public String mainPage(Model model) {
        boolean isAuthenticated = userUtilities.isLoggedIn();
        model.addAttribute("authenticated", isAuthenticated);
        model.addAttribute("urlRequest", new UrlRequest());
        return "index";
    }

    @GetMapping("/secured")
    public String securedPage() {
        return "secured";
    }

}
