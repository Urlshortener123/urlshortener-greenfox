package com.example.demo;

import com.example.demo.DTO.CreateUserRequest;
import com.example.demo.controllers.RegistrationController;
import com.example.demo.models.User;
import com.example.demo.services.EmailService;
import com.example.demo.services.UserService;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;

@ExtendWith(MockitoExtension.class)
public class RegistrationControllerTest {
    @Mock
    private UserService userService;
    @Mock
    private EmailService emailService;
    @Mock
    private Model model;
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;
    @InjectMocks
    private RegistrationController registrationController;

    @Test
    void testRegisterForm_UserNotLoggedIn() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false); //Mock the isAuthenticated method to return false
        SecurityContextHolder.setContext(securityContext);

        String view = registrationController.registerForm(); //Calls the registerForm method
        assertEquals("register", view); //Check that the view returned is /register
    }

    @Test
    void testRegisterForm_UserLoggedIn() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true); //Mock the isAuthenticated method to return true
        SecurityContextHolder.setContext(securityContext);
        String view = registrationController.registerForm(); //same as before
        assertEquals("redirect:/index", view); //same as before just the returned view is /index
    }

    @Test
    void testRegisterSubmit_UserAlreadyExists() {
        CreateUserRequest createUserRequestTest = createUserRequestForTest();
        when(userService.selectUser(createUserRequestTest.getUsername())).thenReturn(new User()); //Mock selectUser to return a User object, in a way that the user already exist
        String view = registrationController.registerSubmit(createUserRequestTest, model); //Calls the registerSubmit method

        verify(userService, times(1)).selectUser(createUserRequestTest.getUsername()); //call the selectUser one time
        verify(userService, times(0)).addUser(any(CreateUserRequest.class)); //do not call the addUser!
        //assertEquals("User already exists", model.getAttribute("errorMessage"));

        assertEquals("register", view); //Check that the view returned is /register
        verify(model, times(1)).addAttribute(eq("errorMessage"), eq("User already exists"));
    }

    @Test
    void testRegisterSubmit_SuccessfulRegistration() {
        CreateUserRequest createUserRequestTest = createUserRequestForTest();
        when(userService.selectUser(createUserRequestTest.getUsername())).thenReturn(null); //Mock selectUser to return null, so in a way that the user does not exist
        String view = registrationController.registerSubmit(createUserRequestTest, model); //Calls the registerSubmit method

        verify(userService, times(2)).selectUser(createUserRequestTest.getUsername()); //call the selectUser 2 times
        verify(userService, times(1)).addUser(createUserRequestTest); //call the addUser one time
        assertEquals("index", view); //Check that the view returned is /index
        verify(model, times(1)).addAttribute(eq("successMessage"), eq("Registration is successful"));
    }

    @Test
    void emailVerification_emailAlreadyVerified() {
        CreateUserRequest createUserRequestTest = createUserRequestForTest();
        when(userService.emailIsVerified(createUserRequestTest.getEmail())).thenReturn(true);
        String view = registrationController.registerSubmit(createUserRequestTest, model); //Calls the registerSubmit method

        verify(userService, times(0)).addUser(any(CreateUserRequest.class)); //do not call the addUser!
        verify(model, times(1)).addAttribute(eq("errorMessage"), eq("E-mail was already verified by another user"));
        assertEquals("register", view); //Check that the view returned is /register
    }

    @Test
    void emailVerification_emailNotVerifiedYet() throws Exception {
        CreateUserRequest createUserRequestTest = createUserRequestForTest();
        //Simulate a login with Spring Security default login, and expecting an error, as the user is not verified
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(registrationController).build();
        mockMvc.perform(formLogin("/login").user(createUserRequestTest.getUsername()).password(createUserRequestTest.getPassword())).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void emailVerification_verificationEmailIsTriggered() throws MessagingException {
        CreateUserRequest createUserRequestTest = createUserRequestForTest();
        registrationController.registerSubmit(createUserRequestTest, model); //Calls the registerSubmit method

        verify(emailService, times(1)).sendEmail("[URL Shortener] Please verify your registration!", createUserRequestTest.getEmail(), createUserRequestTest.getUsername(), null);
    }

    private CreateUserRequest createUserRequestForTest() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("testuser");
        createUserRequest.setPassword("password");
        createUserRequest.setEmail("test@email.com");
        return createUserRequest;
    }

}