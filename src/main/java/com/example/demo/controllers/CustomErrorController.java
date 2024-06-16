package com.example.demo.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.GetMapping;

public class CustomErrorController implements ErrorController {
    @GetMapping("/error")
    public String handleError() {
        return "error";
    }

    /*@Override
    public String getErrorPath() {
        return "/error";
    }*/
}
