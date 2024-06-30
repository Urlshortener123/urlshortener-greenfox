package com.example.demo.controllers;

import com.example.demo.DTO.UrlRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class MainController {

    @GetMapping({"/", "/index"})
    public ModelAndView mainPage() {
        ModelAndView modelAndView = new ModelAndView();
        UrlRequest urlRequest = new UrlRequest();
        modelAndView.addObject("urlRequest", urlRequest);
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @GetMapping("/secured")
    public String securedPage() {
        return "secured";
    }

}
