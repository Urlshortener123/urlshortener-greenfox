package com.example.demo.services;

import com.example.demo.DTO.BlockerResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

@Service
@Slf4j
@RequiredArgsConstructor
public class BlockerService {

    private final BlockerResponseService blockerResponseService;

    @Value("${vtdoc.apikey}")
    private String apiKey;

    private static final Integer MALICIOUS_COUNT_THRESHOLD = 3;

    public boolean isMalicious(String url) {
        boolean isMalicious = false;
        String errorMessage = "Error getting the API response...";
        try {
            String cleanUrl =  url.replaceAll("http(s)?://|www\\.|/.*", "");
            Call<BlockerResponse> callSync = blockerResponseService.fetchDomain(cleanUrl, apiKey);
            Response<BlockerResponse> response = callSync.execute();

            if (response.isSuccessful()) {
                isMalicious = getMaliciousScore(response.body()) > MALICIOUS_COUNT_THRESHOLD;
            } else {
                throw new RuntimeException(response.errorBody().string());
            }
        } catch (Exception e) {
            log.error(errorMessage, e);
        }
        return isMalicious;
    }

    private int getMaliciousScore(BlockerResponse blockerResponse) {
        return blockerResponse.getDomainData().getDomainAttribute().getDomainStat().get("malicious");
    }

}
