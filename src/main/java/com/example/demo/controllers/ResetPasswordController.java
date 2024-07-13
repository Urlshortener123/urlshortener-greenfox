package com.example.demo.controllers;

import com.example.demo.DTO.ResetPasswordRequestDto;
import com.example.demo.services.ResetPasswordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ResetPasswordController {
    private final ResetPasswordService resetPasswordService;

    @GetMapping("/resetPassword")
    public String showResetPasswordForm() {
        return "reset_password";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestParam String email, Model model) {
        try {
            resetPasswordService.createResetPasswordRequest(email);
            model.addAttribute("successMessage", "Reset password email sent successfully.");
        } catch (Exception e) {
            log.error("Failed to send reset password request e-mail...", e);
            model.addAttribute("errorMessage", "An error occurred. Please try again.");
        }
        return "reset_password";
    }

    @GetMapping("/updatePassword")
    public String showUpdatePasswordForm(@RequestParam("username") String username, @RequestParam("hash") String hash, Model model) {
        model.addAttribute("username", username);
        model.addAttribute("hash", hash);
        return "update_password";
    }

    @PostMapping("/updatePassword")
    public String updatePassword(@ModelAttribute ResetPasswordRequestDto resetPasswordRequestDto, Model model) {
        try {
            resetPasswordService.updatePassword(resetPasswordRequestDto.getUsername(), resetPasswordRequestDto.getHash(), resetPasswordRequestDto.getNewPassword());
            model.addAttribute("successMessage", "Password updated successfully");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "update_password";
        }
    }
}
