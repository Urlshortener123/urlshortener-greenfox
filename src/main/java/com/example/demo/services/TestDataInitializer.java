package com.example.demo.services;

import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class TestDataInitializer {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initializeTestUser() {
        log.info("TestDataInitializer is running...");
        if (userRepository.findByUsername("testuser") == null) {
            User test = new User();
            test.setUsername("testuser");
            test.setPassword(passwordEncoder.encode("password"));
            test.setRoles(initializeTestRole("ROLE_USER"));
            test.setEmailVerified(true);
            userRepository.save(test);
            log.info("Created testuser...");
        }
    }

    public List<Role> initializeTestRole(String roleName) {
        List<Role> testRoleList = new ArrayList<>();
        Role role = roleRepository.findByRoleName(roleName);
        if (role == null) {
            // Add new Role if it doesn't exist
            Role testRole = new Role();
            testRole.setRoleName(roleName);
            roleRepository.save(testRole);
            testRoleList.add(testRole);
        } else {
            testRoleList.add(role);
        }
        return testRoleList;
    }

}
