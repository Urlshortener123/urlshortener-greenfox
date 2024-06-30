package com.example.demo;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableEncryptableProperties
public class UrlshortenerGreenfoxApplication {

	public static void main(String[] args) {
		SpringApplication.run(UrlshortenerGreenfoxApplication.class, args);
	}

}
