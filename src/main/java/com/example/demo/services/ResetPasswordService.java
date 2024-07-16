package com.example.demo.services;

import com.example.demo.models.ResetPasswordRequest;
import com.example.demo.models.User;
import com.example.demo.repositories.ResetPasswordRequestRepository;
import com.example.demo.repositories.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResetPasswordService {
    private final UserRepository userRepository;
    private final ResetPasswordRequestRepository resetPasswordRequestRepository;
    private final ResetPasswordEmailService resetPasswordEmailService;
    private final PasswordEncoder passwordEncoder;

    @Value("${domain.name}")
    private String domainName;

    @Transactional
    public void createResetPasswordRequest(String email) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        if (!user.getEmailVerified()) {
            throw new IllegalArgumentException("Email is not verified");
        }

        String hash = UUID.randomUUID().toString();
        ResetPasswordRequest request = new ResetPasswordRequest(null, hash, user, LocalDateTime.now());
        resetPasswordRequestRepository.save(request);

        String resetLink = String.format("%s/updatePassword?username=%s&hash=%s", domainName, user.getUsername(), hash);
        try {
            resetPasswordEmailService.sendResetPasswordEmail("Password Reset Request", user.getEmail(), user.getUsername(), resetLink);
        } catch (MessagingException e) {
            log.error("Failed to send reset password email to user '{}'", user.getUsername(), e);
            throw new RuntimeException("Failed to send reset password email", e);
        }
    }

    @Transactional
    public void updatePassword(String username, String hash, String newPassword) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("Invalid username");
        }

        ResetPasswordRequest resetRequest = resetPasswordRequestRepository.findByUserAndHash(user, hash)
                .orElseThrow(() -> new IllegalArgumentException("Invalid hash"));

        validateTokenNotExpired(resetRequest);

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        resetPasswordRequestRepository.deleteByUser(user);
    }

    private void validateTokenNotExpired(ResetPasswordRequest resetRequest) {
        if (resetRequest.getCreationDate().isBefore(LocalDateTime.now().minusDays(1))) {
            resetPasswordRequestRepository.delete(resetRequest);
            throw new IllegalArgumentException("Hash expired");
        }
    }
}