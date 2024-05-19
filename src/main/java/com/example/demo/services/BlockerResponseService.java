package com.example.demo.services;

import com.example.demo.DTO.BlockerResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BlockerResponseService {

    @GET("domains/{domain}")
    Call<BlockerResponse> fetchMaliciousScore(@Path("domain") String domainName, @Query("x-apikey") String apiKey);

}
