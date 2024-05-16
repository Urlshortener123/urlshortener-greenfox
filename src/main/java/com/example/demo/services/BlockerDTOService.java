package com.example.demo.services;

import com.example.demo.DTO.BlockerDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
public interface BlockerDTOService {

    @GET("domains/{domain}")
    Call<BlockerDTO> fetchMaliciousScore(@Path("domain") String domainName, @Query("x-apikey") String apiKey);

}
