package com.example.demo;

import com.example.demo.controllers.UrlController;
import com.example.demo.models.ShortenedUrl;
import com.example.demo.repositories.LinkRepository;
import com.example.demo.services.LinkServices;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class UrlshortenerGreenfoxApplicationTests {
    @Test
    void contextLoads() {
    }

    @Autowired
    private LinkServices service;
    @MockBean
    private LinkRepository linkRepository;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        ShortenedUrl shortenedUrl = new ShortenedUrl("http://testingurl.com", "testuuid", 1L);
        linkRepository.save(shortenedUrl);
        //if we comment out  the line below  the last test will fail, h2 database is not working
        Mockito.when(linkRepository.findByUuid("testuuid")).thenReturn(shortenedUrl);
    }

    @Test
    public void getLinkTestService() {
        String testUrl = "http://testingurl.com";
        ShortenedUrl shortenedUrl = service.findByUuid("testuuid");
        assertEquals(testUrl, shortenedUrl.getUrl());
    }

    @Test
    public void getLinkTestFromUrl() throws Exception {
        this.mockMvc.perform(get("/r/testuuid")).andExpect((redirectedUrl(linkRepository.findByUuid("testuuid").getUrl())));
    }


}
