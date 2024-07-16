package com.example.demo.services;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResetPasswordEmailService {
    private final EmailService emailService;
    private static final String RESET_PASSWORD_TEMPLATE =
            "<p>Dear ${userName}!</p><br>"
                    + "<p>We received a request to reset your password.</p>"
                    + "<p>Please reset your password with the link below:</p>"
                    + "<a href=\"${resetLink}\">${resetLink}</a><br><br>"
                    + "<p>If you did not request a password reset, please ignore this email.</p>"
                    + "<p>Best Regards,</p>"
                    + "<p>URL Shortener Team</p>";

    public void sendResetPasswordEmail(String subject, String emailTo, String userName, String resetLink) throws MessagingException {
        String content = RESET_PASSWORD_TEMPLATE
                .replace("${userName}", userName)
                .replace("${resetLink}", resetLink);
        emailService.sendEmail(subject, emailTo, content);
    }
}
