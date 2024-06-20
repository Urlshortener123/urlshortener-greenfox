package com.example.demo;

import com.example.demo.DTO.CreateUserRequest;
import com.example.demo.controllers.RegistrationController;
import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.models.UserVerificationToken;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.repositories.UserVerificationTokenRepository;
import com.example.demo.services.BlockerService;
import com.example.demo.services.EmailService;
import com.example.demo.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RegistrationVerificationTest {

    @Autowired
    private UserVerificationTokenRepository userVerificationTokenRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserService userService;
    @MockBean
    private EmailService emailService;
    @MockBean
    private BlockerService blockerService;
    @Autowired
    private MockMvc mockMvc;
    @Mock
    private RedirectAttributes redirectAttributes;
    @InjectMocks
    private RegistrationController registrationController;

    private static final String USERNAME = "testuser";

    @BeforeEach
    void init() {
        Role userRole = new Role();
        userRole.setRoleName("ROLE_USER");
        roleRepository.save(userRole);
        CreateUserRequest createUserRequest = createUserRequestForH2Test();
        userService.addUser(createUserRequest);
    }

    @Test
    void adding_user_successful() {
        User newUser = userService.selectUser(USERNAME);
        assertEquals(USERNAME, newUser.getUsername());
    }

    @Test
    void token_creation_successful() {
        User newUser = userService.selectUser(USERNAME);
        assertNotNull(userVerificationTokenRepository.findByUser(newUser));
    }

    @Test
    void email_validation_and_token_deletion_successful() {
        UserVerificationToken userVerificationToken = userVerificationTokenRepository.findByUser(userRepository.findByUsername(USERNAME));
        userService.validateUser(userVerificationToken);
        User newUser = userVerificationToken.getUser();
        assertTrue(newUser.getEmailVerified());
        assertNull(userVerificationTokenRepository.findByUser(newUser));
    }

    @Test
    void user_verification_successful() throws Exception {
        User newUser = userService.selectUser(USERNAME);
        UserVerificationToken userVerificationToken = userVerificationTokenRepository.findByUser(newUser);
        this.mockMvc.perform(get("/verify")
                        .param("hash", userVerificationToken.getHash()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attribute("verificationMessage", "Your registration has been verified, now you are able to log in!"));
    }

    private CreateUserRequest createUserRequestForH2Test() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(USERNAME);
        createUserRequest.setPassword("password");
        createUserRequest.setEmail("test@email.com");
        return createUserRequest;
    }

}
