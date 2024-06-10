package com.example.demo.services;

import com.example.demo.DTO.BlockerResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface BlockerResponseService {

    @GET("api/v3/domains/{domain}")
    Call<BlockerResponse> fetchDomain(@Path("domain") String domainName, @Header("x-apikey") String apiKey);

}