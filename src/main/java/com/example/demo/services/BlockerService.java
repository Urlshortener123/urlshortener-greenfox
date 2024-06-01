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

    @Value("${VTDOC_APIKEY}")
    private String apiKey;

    public boolean isMalicious(String url) {
        boolean isMalicious = false;
        String errorMessage = "Error getting the API response...";
        try {
            String cleanUrl =  url.replaceAll("http(s)?://|www\\.|/.*", "");
            Call<BlockerResponse> callSync = blockerResponseService.fetchDomain(cleanUrl, apiKey);
            Response<BlockerResponse> response = callSync.execute();
            BlockerResponse blockerResponse = response.body();
            if (blockerResponse == null) {
                log.error(errorMessage);
            } else if (getMaliciousScore(blockerResponse) > 3) {
                isMalicious = true;
            }
        } catch (Exception e) {
            log.error(errorMessage);
        }
        return isMalicious;
    }

    private int getMaliciousScore(BlockerResponse blockerResponse) {
        return blockerResponse.getDomainData().getDomainAttribute().getDomainStat().get("malicious");
    }

}
