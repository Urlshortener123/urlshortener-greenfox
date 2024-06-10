package com.example.demo;

import com.example.demo.models.ShortenedUrl;
import com.example.demo.repositories.LinkRepository;
import com.example.demo.services.BlockerService;
import com.example.demo.services.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@SpringBootTest
@AutoConfigureMockMvc
class UrlShorteningTest {

    @Autowired
    private LinkRepository linkRepository;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BlockerService blockerService;
    @MockBean
    private EmailService emailService;

    private static final String UUID = "testuuid";
    private static final String TEST_URL = "http://testingurl.com";

    @BeforeEach
    void init() {
        ShortenedUrl shortenedUrl = new ShortenedUrl(1L, TEST_URL, UUID, null, null);
        linkRepository.save(shortenedUrl);
    }

    @Test
    void link_shortening_successful() {
        ShortenedUrl shortenedUrl = linkRepository.findByUuid(UUID);
        assertEquals(TEST_URL, shortenedUrl.getUrl());
    }

    @Test
    void reverting_shortened_link_successful() throws Exception {
        this.mockMvc.perform(get("/r/" + UUID)).andExpect((redirectedUrl(linkRepository.findByUuid(UUID).getUrl())));
    }

    @Test
    void malicious_url_blocking_successful() throws Exception {
        Mockito.when(blockerService.isMalicious(TEST_URL)).thenReturn(true);
        MockHttpServletResponse result = mockMvc.perform(post("/shortUrl")
                        .param("url", TEST_URL)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(model().attributeExists("maliciousError"))
                .andReturn().getResponse();
        assertEquals(200, result.getStatus());
    }

}
