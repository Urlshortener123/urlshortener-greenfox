package com.example.demo;

import com.example.demo.config.HTTPClientConfig;
import com.example.demo.services.BlockerService;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
@WireMockTest(httpPort = 8099)
@SpringBootTest(classes = { BlockerService.class, HTTPClientConfig.class})
@TestPropertySource(properties = {"vtdoc.url = http://localhost:8099"})
public class BlockerServiceTest {

    @Autowired
    private BlockerService blockerService;

    private static final String URL_TO_TEST = "test.com";
    private static final String URL_CHECK_PATH = "/api/v3/domains/{domain}";


    @Test
    void checked_url_clean() {
        stubFor(WireMock.get(urlPathTemplate(URL_CHECK_PATH))
                .withPathParam("domain", WireMock.equalTo(URL_TO_TEST))
                .willReturn(ok(getResponse(false))));

        boolean malicious = blockerService.isMalicious(URL_TO_TEST);

        assertFalse(malicious);
    }

    @Test
    void checked_url_malicious() {
        stubFor(WireMock.get(urlPathTemplate(URL_CHECK_PATH))
                .withPathParam("domain", WireMock.equalTo(URL_TO_TEST))
                .willReturn(ok(getResponse(true))));

        boolean malicious = blockerService.isMalicious(URL_TO_TEST);

        assertTrue(malicious);
    }

    @Test
    void url_check_unsuccessful_response() {
        stubFor(WireMock.get(urlPathTemplate(URL_CHECK_PATH))
                .withPathParam("domain", WireMock.equalTo(URL_TO_TEST))
                .willReturn(badRequest().withBody("Api key invalid")));

        boolean malicious = blockerService.isMalicious(URL_TO_TEST);

        assertFalse(malicious);
    }

    @Test
    void url_check_malformed_response() {
        stubFor(WireMock.get(urlPathTemplate(URL_CHECK_PATH))
                .withPathParam("domain", WireMock.equalTo(URL_TO_TEST))
                .willReturn(ok("{}")));

        boolean malicious = blockerService.isMalicious(URL_TO_TEST);

        assertFalse(malicious);
    }


    private String getResponse(boolean malicious) {
        int count = malicious ? 8 : 0;

        String response = """
                {
                  "data": {
                    "attributes": {
                      "last_analysis_stats": {
                        "malicious": %d
                      }
                    }
                  }
                }
                """;

        return String.format(response, count);
    }
}
