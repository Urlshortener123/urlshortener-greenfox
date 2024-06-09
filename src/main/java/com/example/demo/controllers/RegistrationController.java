package com.example.demo.controllers;

import com.example.demo.DTO.CreateUserRequest;
import com.example.demo.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RegistrationController {
    private final UserService userService;

    private boolean isLoggedIn() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.isAuthenticated();
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        //Is the user logged in?
        if (isLoggedIn()) {
            return "redirect:/index";
        }
        return "register";
    }

    @PostMapping("/register")
    public String registerSubmit(CreateUserRequest createUserRequest, Model model) {
        //Does the user already exist?
        if (userService.selectUser(createUserRequest.getUsername()) != null) {
            model.addAttribute("errorMessage", "User already exists");
            return "register";
        }
        //ROLE_USER role
        userService.addUser(createUserRequest);

        //user should be redirected to the /login page
        model.addAttribute("successMessage", "Registration is successful");
        return "redirect:/index";
    }
}