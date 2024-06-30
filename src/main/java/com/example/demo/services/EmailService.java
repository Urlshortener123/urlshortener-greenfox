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
public class EmailService {

    @Value("${spring.mail.host}")
    private String hostName;
    @Value("${spring.mail.port}")
    private String hostPort;
    @Value("${spring.mail.username}")
    private String hostUser;
    @Value("${spring.mail.password}")
    private String hostPassword;
    @Value("${domain.name}")
    private String domainName;

    private static String htmlContent =
            "<p>Dear ${userName}!</p><br>"
                    + "<p>Welcome to URL Shortener!</p>"
                    + "<p>Please verify your registration with the link below:</p>"
                    + "<a href=\"${verificationLink}\">${verificationLink}</a><br><br>"
                    + "<p>Thank you!</p>"
                    + "<p>Best Regards,</p>"
                    + "<p>URL Shortener Team</p>";

    public void sendEmail(String subject, String emailTo, String userNameTo, String hashKey) throws MessagingException {
        JavaMailSenderImpl emailSender = new JavaMailSenderImpl();
        emailSender.setHost(hostName);
        emailSender.setPort(Integer.parseInt(hostPort));
        emailSender.setUsername(hostUser);
        emailSender.setPassword(hostPassword);
        htmlContent = htmlContent.replace("${userName}", userNameTo);
        htmlContent = htmlContent.replace("${verificationLink}", domainName + "/verify?hash=" + hashKey);

        Properties properties = emailSender.getJavaMailProperties();
        properties.put("mail.transport.protocol", "smtp");

        MimeMessage mimeMessage = emailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");
        mimeMessageHelper.setFrom("system@urlshortener.com");
        mimeMessageHelper.setText(htmlContent, true);
        mimeMessageHelper.setTo(emailTo);
        mimeMessageHelper.setSubject(subject);

        emailSender.send(mimeMessage);
    }

}
