package com.example.demo.services;

import com.example.demo.models.ResetPasswordRequest;
import com.example.demo.models.User;
import com.example.demo.repositories.ResetPasswordRequestRepository;
import com.example.demo.repositories.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResetPasswordService {
    private final UserRepository userRepository;
    private final ResetPasswordRequestRepository resetPasswordRequestRepository;
    private final ResetPasswordEmailService resetPasswordEmailService;
    private final PasswordEncoder passwordEncoder;

    @Value("${domain.name}")
    private String domainName;

    public void createResetPasswordRequest(User user) {
        String hash = UUID.randomUUID().toString();
        ResetPasswordRequest request = new ResetPasswordRequest(null, hash, user, LocalDateTime.now());
        resetPasswordRequestRepository.save(request);

        String resetLink = domainName + "/updatePassword?username=" + user.getUsername() + "&hash=" + hash;
        try {
            resetPasswordEmailService.sendResetPasswordEmail("Password Reset Request", user.getEmail(), user.getUsername(), resetLink);
        } catch (MessagingException e) {
            e.printStackTrace();
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

        validateToken(resetRequest);

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        resetPasswordRequestRepository.deleteByUser(user);
    }

    public User verifyResetToken(String hashKey) {
        ResetPasswordRequest resetRequest = resetPasswordRequestRepository.findByHash(hashKey)
                .orElseThrow(() -> new IllegalArgumentException("Invalid hash"));

        validateToken(resetRequest);

        return resetRequest.getUser();
    }

    private void validateToken(ResetPasswordRequest resetRequest) {
        if (resetRequest.getCreationDate().isBefore(LocalDateTime.now().minusDays(1))) {
            resetPasswordRequestRepository.delete(resetRequest);
            throw new IllegalArgumentException("Hash expired");
        }
    }
}