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

    public void sendEmail(String subject, String emailTo, String content) throws MessagingException {
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
        mimeMessageHelper.setText(content, true);

        emailSender.send(mimeMessage);
    }

}
