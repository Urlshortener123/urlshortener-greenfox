package com.example.demo;

import com.example.demo.DTO.CreateUserRequest;
import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private UserService underTest;

    @Test
    void save_user_successful() {
        String username = "testusername";
        String password = "testpassword";
        String encodedPassword = "testpasswordEncoded";
        Mockito.when(passwordEncoder.encode(eq(password))).thenReturn(encodedPassword);
        Mockito.when(roleRepository.findByRoleName(eq("ROLE_USER"))).thenReturn(new Role(1L, "ROLE_USER", Collections.emptyList()));
        ArgumentCaptor<User> userArgument = ArgumentCaptor.forClass(User.class);

        CreateUserRequest request = new CreateUserRequest();
        request.setUsername(username);
        request.setPassword(password);

        underTest.addUser(request);

        Mockito.verify(userRepository, Mockito.times(1)).save(userArgument.capture());
        User user = userArgument.getValue();
        assertEquals(username, user.getUsername());
        assertEquals(encodedPassword, user.getPassword());
    }

}
