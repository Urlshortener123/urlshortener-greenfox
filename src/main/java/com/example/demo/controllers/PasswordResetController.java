/*package com.example.demo.controllers;

import com.example.demo.models.User;
import com.example.demo.services.EmailService;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

public class PasswordResetController {
    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/resetPassword")
    public String resetPasswordRequest(@RequestParam("email") String email, Model model) {
        User user = userService.findByEmail(email);
        if (user == null) {
            model.addAttribute("error", "No user found with this email.");
            return "reset_password_request";
        }

        String hash = UUID.randomUUID().toString();
        userService.createPasswordResetRequest(user.getId(), hash);

        String resetLink = "http://localhost:8080/resetPassword?username=" + user.getUsername() + "&hash=" + hash;
        emailService.sendEmail(email, "Reset Password", "Click the link to reset your password: " + resetLink);

        model.addAttribute("message", "Password reset link has been sent to your email.");
        return "reset_password_request";
    }

    @GetMapping("/resetPassword")
    public String resetPasswordForm(@RequestParam("username") String username, @RequestParam("hash") String hash, Model model) {
        if (!userService.isPasswordResetRequestValid(username, hash)) {
            model.addAttribute("error", "Invalid or expired password reset link.");
            return "reset_password_request";
        }
        model.addAttribute("username", username);
        model.addAttribute("hash", hash);
        return "reset_password_form";
    }

    @PostMapping("/updatePassword")
    public String updatePassword(@RequestParam("username") String username, @RequestParam("hash") String hash, @RequestParam("password") String password, Model model) {
        if (!userService.isPasswordResetRequestValid(username, hash)) {
            model.addAttribute("error", "Invalid or expired password reset link.");
            return "reset_password_form";
        }

        userService.updatePassword(username, password);
        userService.deletePasswordResetRequests(username);

        model.addAttribute("message", "Password has been successfully reset. You can now log in.");
        return "login";
    }
}
*/