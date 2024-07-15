package com.example.demo.DTO;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
@NoArgsConstructor
public class UrlRequest {

    @NotEmpty(message = "No URL is provided!")
    @URL(message = "Not a URL, please provide a URL in correct format!")
    private String url;

}
