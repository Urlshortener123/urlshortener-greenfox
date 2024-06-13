package com.example.demo;

import com.example.demo.models.ShortenedUrl;
import com.example.demo.repositories.LinkRepository;
import com.example.demo.services.LinkService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
class UrlshortenerGreenfoxApplicationTests {

    @Autowired
    private LinkService linkService;
    @Autowired
    private LinkRepository linkRepository;
    @Autowired
    private MockMvc mockMvc;

    private static final String UUID = "testuuid";
    private static final String TEST_URL = "http://testingurl.com";

    @BeforeEach
    void setUp() {
        ShortenedUrl shortenedUrl = new ShortenedUrl(1L, TEST_URL, UUID, null, null, 0);
        linkRepository.save(shortenedUrl);
    }

    @Test
    void getLinkTestService() {
        ShortenedUrl shortenedUrl = linkService.findByUuid(UUID);
        assertEquals(TEST_URL, shortenedUrl.getUrl());
    }

    @Test
    void getLinkTestFromUrl() throws Exception {
        this.mockMvc.perform(get("/r/testuuid")).andExpect((redirectedUrl(linkRepository.findByUuid(UUID).getUrl())));
    }

    @Test
    void urlClickCountIncrements() throws Exception {
        ShortenedUrl shortenedUrl = linkRepository.findByUuid(UUID); //we have a shortened url
        int initialClickCount = shortenedUrl.getClickCount();

        this.mockMvc.perform(get("/r/" + UUID)); //Mocking of the opening/clicking

        shortenedUrl = linkRepository.findByUuid(UUID); // Reload the entity
        int updatedClickCount = shortenedUrl.getClickCount();

        assertEquals(initialClickCount + 1, updatedClickCount);
    }

}
