package com.example.demo.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
@RequiredArgsConstructor
public class ResetPasswordEmailService {
    @Value("${spring.mail.host}")
    private String hostName;
    @Value("${spring.mail.port}")
    private String hostPort;
    @Value("${spring.mail.username}")
    private String hostUser;
    @Value("${spring.mail.password}")
    private String hostPassword;

    private static final String RESET_PASSWORD_TEMPLATE =
            "<p>Dear ${userName}!</p><br>"
                    + "<p>We received a request to reset your password.</p>"
                    + "<p>Please reset your password with the link below:</p>"
                    + "<a href=\"${resetLink}\">${resetLink}</a><br><br>"
                    + "<p>If you did not request a password reset, please ignore this email.</p>"
                    + "<p>Best Regards,</p>"
                    + "<p>URL Shortener Team</p>";

    public void sendResetPasswordEmail(String subject, String emailTo, String userName, String resetLink) throws MessagingException {
        JavaMailSenderImpl emailSender = new JavaMailSenderImpl();
        emailSender.setHost(hostName);
        emailSender.setPort(Integer.parseInt(hostPort));
        emailSender.setUsername(hostUser);
        emailSender.setPassword(hostPassword);

        Properties properties = emailSender.getJavaMailProperties();
        properties.put("mail.transport.protocol", "smtp");

        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");
        mimeMessageHelper.setFrom("system@urlshortener.com");
        mimeMessageHelper.setTo(emailTo);
        mimeMessageHelper.setSubject(subject);

        String personalizedTemplate = RESET_PASSWORD_TEMPLATE
                .replace("${userName}", userName)
                .replace("${resetLink}", resetLink);

        mimeMessageHelper.setText(personalizedTemplate, true);

        emailSender.send(mimeMessage);
    }
}
