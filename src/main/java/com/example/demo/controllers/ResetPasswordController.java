package com.example.demo.controllers;

import com.example.demo.models.User;
import com.example.demo.services.ResetPasswordService;
import com.example.demo.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ResetPasswordController {
    private final UserService userService;
    private final ResetPasswordService resetPasswordService;

    @GetMapping("/resetPassword")
    public String showResetPasswordForm() {
        return "reset_password";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestParam String email, Model model) {
        User user = userService.findByEmail(email);

        if (user == null) {
            model.addAttribute("errorMessage", "User not found");
            return "reset_password";
        }

        if (!user.getEmailVerified()) {
            model.addAttribute("errorMessage", "Email is not verified");
            return "reset_password";
        }

        try {
            resetPasswordService.createResetPasswordRequest(user);
            model.addAttribute("successMessage", "Reset password email sent successfully.");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "An error occurred. Please try again.");
        }
        return "reset_password";
    }

    @GetMapping("/updatePassword")
    public String verifyResetToken(@RequestParam("hash") String hash, Model model) {
        try {
            User user = resetPasswordService.verifyResetToken(hash);
            model.addAttribute("username", user.getUsername());
            model.addAttribute("hash", hash);
            return "update_password";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/error";
        }
    }

    @PostMapping("/updatePassword")
    public String updatePassword(@RequestParam String username, @RequestParam String hash, @RequestParam String newPassword, Model model) {
        try {
            resetPasswordService.updatePassword(username, hash, newPassword);
            model.addAttribute("successMessage", "Password updated successfully");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "update_password";
        }
    }
}
