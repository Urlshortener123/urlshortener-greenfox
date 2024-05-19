package com.example.demo.services;

import com.example.demo.DTO.BlockerResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class BlockerService {

    @Autowired
    private BlockerResponseService blockerResponseService;

    @Value("${VTDOC_APIKEY}")
    private String apiKey;

    public boolean IsMalicious(String url) {
        boolean isMalicious = false;
        try {
            Call<BlockerResponse> callSync = blockerResponseService.fetchMaliciousScore(url, apiKey);
            Response<BlockerResponse> response = callSync.execute();
            BlockerResponse blockerResponse = response.body();
            if (blockerResponse == null) {
                log.error("Error getting the API response...");
            } else if (blockerResponse.getMaliciousScore() > 3) {
                isMalicious = true;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return isMalicious;
    }

}
