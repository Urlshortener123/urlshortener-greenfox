package com.example.demo.services;

import com.example.demo.DTO.CreateUserRequest;
import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.models.UserVerificationToken;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.repositories.UserVerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserVerificationTokenRepository userVerificationTokenRepository;
    //private final ResetPasswordRequestRepository resetPasswordRequestRepository;
    //private final EmailService emailService;

    public void addUser(CreateUserRequest createUserRequest) {
        User user = new User();
        user.setUsername(createUserRequest.getUsername());
        user.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));
        user.setEmail(createUserRequest.getEmail());
        user.setEmailVerified(false);
        Role userRole = roleRepository.findByRoleName("ROLE_USER");
        if (userRole == null) {
            throw new IllegalStateException("ROLE_USER role does not exist in the database.");
        }
        // Assign the existing role to the new user
        user.getRoles().add(userRole);
        userRepository.save(user);

        // Adding an entry to pending_user_verifications
        UserVerificationToken userVerificationToken = new UserVerificationToken();
        userVerificationToken.setHash(UUID.randomUUID().toString());
        userVerificationToken.setUser(user);
        userVerificationTokenRepository.save(userVerificationToken);
    }

    public User selectUser(String username) {
        return userRepository.findByUsername(username);
    }

    public Boolean emailIsVerified(String emailAddress) {
        return userRepository.findByEmail(emailAddress) != null && userRepository.findByEmail(emailAddress).getEmailVerified();
    }

    public String selectVerificationToken(User user) {
        return userVerificationTokenRepository.findByUser(user).getHash();
    }

    public UserVerificationToken selectVerificationTokenByHash(String hash) {
        return userVerificationTokenRepository.findByHash(hash);
    }

    public void validateUser(UserVerificationToken userVerificationToken) {
        User actUser = userVerificationToken.getUser();
        actUser.setEmailVerified(true);
        userRepository.save(actUser);
        userVerificationTokenRepository.delete(userVerificationToken);
    }

    /*public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void createPasswordResetRequest(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            String hash = UUID.randomUUID().toString();
            ResetPasswordRequest request = new ResetPasswordRequest();
            request.setUserId(user.getId());
            request.setHash(hash);
            resetPasswordRequestRepository.save(request);

            String resetLink = "http://localhost:8080/resetPassword?username=" + user.getUsername() + "&hash=" + hash;
            emailService.sendEmail(user.getEmail(), "Reset Password", "Click the link to reset your password: " + resetLink);
        }
    }

    public boolean isPasswordResetRequestValid(String username, String hash) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return false;
        }
        ResetPasswordRequest request = resetPasswordRequestRepository.findByUserIdAndHash(user.getId(), hash);
        return request != null && request.getCreationDate().isAfter(LocalDateTime.now().minusDays(1));
    }

    public void updatePassword(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
        }
    }

    public void deletePasswordResetRequests(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            resetPasswordRequestRepository.deleteByUserId(user.getId());
        }
    }*/

}