package com.example.demo;

import com.example.demo.DTO.CreateUserRequest;
import com.example.demo.DTO.UrlRequest;
import com.example.demo.services.BlockerService;
import com.example.demo.services.EmailService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class InputValidationTest {

    @Autowired
    private Validator validator;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EmailService emailService;
    @MockBean
    private BlockerService blockerService;

    @Test
    public void userRegistration_inputIsValid_NoConstraintViolations() {
        CreateUserRequest createUserRequestForTest = new CreateUserRequest();
        createUserRequestForTest.setUsername("testuser");
        createUserRequestForTest.setPassword("password");
        createUserRequestForTest.setEmail("test@email.com");
        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(createUserRequestForTest);

        assertTrue(violations.isEmpty());
    }

    @Test
    public void userRegistration_inputIsNull_3_ConstraintViolations() {
        CreateUserRequest createUserRequestForTest = new CreateUserRequest();
        //No username, password, and e-mail is added, so all 3 parameters are missing
        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(createUserRequestForTest);

        assertThat(violations.size()).isEqualTo(3);
    }

    @Test
    public void userRegistration_inputHasNoUsername_1_ConstraintViolation() {
        CreateUserRequest createUserRequestForTest = new CreateUserRequest();
        //No username is added, so 1 parameter is missing
        createUserRequestForTest.setPassword("password");
        createUserRequestForTest.setEmail("test@email.com");
        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(createUserRequestForTest);

        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void userRegistration_inputHasWrongEmail_1_ConstraintViolation() {
        CreateUserRequest createUserRequestForTest = new CreateUserRequest();
        createUserRequestForTest.setUsername("testuser");
        createUserRequestForTest.setPassword("password");
        //Wrong format is used for e-mail address
        createUserRequestForTest.setEmail("dummy_mail");
        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(createUserRequestForTest);

        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void userRegistration_PasswordIsShort_ThrowsError() throws Exception {
        this.mockMvc.perform(post("/register")
                        .param("username", "testuser")
                        //Less then 6 characters are used for e-mail address
                        .param("password", "pass")
                        .param("email", "test@email.com")
                        .with(csrf()))
                .andExpect(model().attributeHasFieldErrors("createUserRequest", "password"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    public void userVerification_MissingHash_ThrowsError() throws Exception {
        this.mockMvc.perform(get("/verify")
                        //No hash is provided for verification
                        .param("hash", "")
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void urlShortening_UrlIsEmpty_ThrowsError() throws Exception {
        this.mockMvc.perform(post("/shortUrl")
                        //No URL is provided
                        .param("url", "")
                        .with(csrf()))
                .andExpect(model().attributeHasFieldErrors("urlRequest", "url"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    public void urlShortening_NoUrlProvided_1_ConstraintViolation() {
        UrlRequest urlRequestForTest = new UrlRequest(); //No URL is added
        Set<ConstraintViolation<UrlRequest>> violations = validator.validate(urlRequestForTest);

        assertThat(violations.size()).isEqualTo(1);
    }

}
