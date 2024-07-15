package com.example.demo.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateUserRequest {

    @NotEmpty(message = "Please provide a username!")
    @Size(max = 10, message = "You can use max. 10 characters as your username!")
    private String username;

    @NotEmpty(message = "Please provide a password!")
    @Size(min = 6, message = "Password must consist of at least 6 characters!")
    private String password;

    @NotEmpty(message = "Please provide an e-mail address!")
    @Email(message = "Please use a valid e-mail format!")
    private String email;

}
