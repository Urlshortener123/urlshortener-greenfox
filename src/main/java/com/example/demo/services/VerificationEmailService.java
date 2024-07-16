package com.example.demo.services;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerificationEmailService {
    private final EmailService emailService;

    @Value("${domain.name}")
    private String domainName;

    private static final String VERIFICATION_TEMPLATE =
            "<p>Dear ${userName}!</p><br>"
                    + "<p>Welcome to URL Shortener!</p>"
                    + "<p>Please verify your registration with the link below:</p>"
                    + "<a href=\"${verificationLink}\">${verificationLink}</a><br><br>"
                    + "<p>Thank you!</p>"
                    + "<p>Best Regards,</p>"
                    + "<p>URL Shortener Team</p>";

    public void sendVerificationEmail(String subject, String emailTo, String userName, String hashKey) throws MessagingException {
        String verificationLink = domainName + "/verify?hash=" + hashKey;
        String content = VERIFICATION_TEMPLATE
                .replace("${userName}", userName)
                .replace("${verificationLink}", verificationLink);
        emailService.sendEmail(subject, emailTo, content);
    }
}
