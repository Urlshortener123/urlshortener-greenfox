package com.example.demo.services;

import com.example.demo.DTO.CreateUserRequest;
import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;


    public void addUser(CreateUserRequest createUserRequest) {

        User user = new User();
        user.setUsername(createUserRequest.getUsername());
        user.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));


        Role roleUser = roleRepository.findByRoleName("ROLE_USER");
        user.getRoles().add(roleUser);
        userRepository.save(user);
    }

    public User selectUser(String username) {
        return userRepository.findByUsername(username);
    }

}