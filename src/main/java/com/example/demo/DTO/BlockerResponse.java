package com.example.demo.DTO;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@RequiredArgsConstructor
@ToString
public class BlockerResponse {

    @SerializedName("data")
    private BlockerData domainData;

}
