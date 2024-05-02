package com.example.demo.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    @GetMapping({"/", "/index"})
    public String mainPage() {
        return "index";
    }

    @GetMapping("/secured")
    public String securedPage() {
        return "secured";
    }

}
