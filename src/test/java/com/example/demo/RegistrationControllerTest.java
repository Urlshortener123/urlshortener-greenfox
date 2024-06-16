package com.example.demo;

import com.example.demo.DTO.CreateUserRequest;
import com.example.demo.controllers.RegistrationController;
import com.example.demo.models.User;
import com.example.demo.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegistrationControllerTest {
    @Mock
    private UserService userService;
    @Mock
    private Model model;
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;
    @InjectMocks
    private RegistrationController registrationController;
    private MockMvc mockMvc;
    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(registrationController).build();
    }

    @Test
    public void testRegisterForm_UserNotLoggedIn() throws Exception {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false); //Mock the isAuthenticated method to return false
        SecurityContextHolder.setContext(securityContext);

        String view = registrationController.registerForm(model); //Calls the registerForm method
        assertEquals("register", view); //Check that the view returned is /register
    }

    @Test
    public void testRegisterForm_UserLoggedIn() throws Exception {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true); //Mock the isAuthenticated method to return true
        SecurityContextHolder.setContext(securityContext);

        String view = registrationController.registerForm(model); //same as before
        assertEquals("redirect:/index", view); //same as before just the returned view is /index
    }

    @Test
    public void testRegisterSubmit_UserAlreadyExists() throws Exception {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("existinguser"); //Creates a CreateUserRequest with a username

        when(userService.selectUser(createUserRequest.getUsername())).thenReturn(new User()); //Mock selectUser to return a User object, in a way that the user already exist
        String view = registrationController.registerSubmit(createUserRequest, model); //Calls the registerSubmit method

        verify(userService, times(1)).selectUser(createUserRequest.getUsername()); //call the selectUser one time
        verify(userService, times(0)).addUser(any(CreateUserRequest.class)); //do not call the addUser!
        //assertEquals("User already exists", model.getAttribute("errorMessage"));

        assertEquals("register", view); //Check that the view returned is /register
        verify(model, times(1)).addAttribute(eq("errorMessage"), eq("User already exists"));
    }


    @Test
    public void testRegisterSubmit_SuccessfulRegistration() throws Exception {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("newuser");
        createUserRequest.setPassword("password"); //Creates a CreateUserRequest with a username and a password

        when(userService.selectUser(createUserRequest.getUsername())).thenReturn(null); //Mock selectUser to return null, so in a way that the user does not exist
        String view = registrationController.registerSubmit(createUserRequest, model); //Calls the registerSubmit method

        verify(userService, times(1)).selectUser(createUserRequest.getUsername()); //call the selectUser one time
        verify(userService, times(1)).addUser(createUserRequest); //call the addUser one time

        assertEquals("index", view); //Check that the view returned is /index
        verify(model, times(1)).addAttribute(eq("successMessage"), eq("Registration is successful"));
    }

}