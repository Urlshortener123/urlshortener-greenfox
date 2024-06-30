package com.example.demo.DTO;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UrlRequest {

    @NotEmpty(message = "No URL is provided!")
    private String url;

}
