package com.example.demo.config;

import com.example.demo.services.BlockerResponseService;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Configuration
public class HTTPClientConfig {

    @Value("${vtdoc.url}")
    private String vtDocUrl;

    @Bean
    public BlockerResponseService blockerDTOService() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(vtDocUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        return retrofit.create(BlockerResponseService.class);
    }

}
