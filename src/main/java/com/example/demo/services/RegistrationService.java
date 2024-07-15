package com.example.demo.services;

import com.example.demo.DTO.CreateUserRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class RegistrationService {

    private final UserService userService;

    public void registerUser(CreateUserRequest createUserRequest) {
        //Does the user already exist?
        String userName = createUserRequest.getUsername();
        if (userService.selectUser(userName) != null) {
            throw new IllegalStateException("User already exists");
        }
        //Check if email was already used
        String userEmail = createUserRequest.getEmail();
        if (userService.emailIsVerified(userEmail)) {
            throw new IllegalStateException("E-mail was already verified by another user");
        }
        //Successful user registration
        userService.addUser(createUserRequest);
    }

}
