package com.example.demo.services;

import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TestDataInitializer {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initializeTestUser() {
        if (userRepository.findByUsername("testuser") == null) {
            User test = new User();
            test.setUsername("testuser");
            test.setPassword(passwordEncoder.encode("password"));
            test.setRoles(initializeTestRole("ROLE_USER"));
            userRepository.save(test);
        }
    }

    public List<Role> initializeTestRole(String roleName) {
        if (roleRepository.findByRoleName(roleName) == null) {
            // Add new Role if it doesn't exist
            Role testRole = new Role();
            testRole.setRoleName(roleName);
            roleRepository.save(testRole);
            // Return a List<Role> with 1 Role (from parameter)
            List<Role> testRoleList = new ArrayList<>();
            testRoleList.add(testRole);
            return testRoleList;
        } else {
            return null;
        }
    }

}
