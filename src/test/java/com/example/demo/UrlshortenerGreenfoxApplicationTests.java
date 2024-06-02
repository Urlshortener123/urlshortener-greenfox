package com.example.demo;

import com.example.demo.models.ShortenedUrl;
import com.example.demo.repositories.LinkRepository;
import com.example.demo.services.LinkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class UrlshortenerGreenfoxApplicationTests {

    /*@Test
    void contextLoads() {
    }

    @Autowired
    private LinkService service;
    @Autowired
    private LinkRepository linkRepository;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        ShortenedUrl shortenedUrl = new ShortenedUrl();
        linkRepository.save(shortenedUrl);
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
    }*/

}
