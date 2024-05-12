package com.example.demo.controllers;

import com.example.demo.models.User;
import com.example.demo.services.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final UserService userService;

    @GetMapping({"/", "/index"})
    public String mainPage(Principal principal, HttpSession session) {
        if (principal != null) {
            User actUser = userService.selectUser(principal.getName());
            session.setAttribute("username", actUser.getUsername());
        }
        return "index";
    }

    @GetMapping("/secured")
    public String securedPage() {
        return "secured";
    }

}
