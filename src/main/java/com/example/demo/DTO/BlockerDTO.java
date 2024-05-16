package com.example.demo.DTO;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@RequiredArgsConstructor
@ToString
public class BlockerDTO {

    @SerializedName("data.id")
    private String domainName;
    @SerializedName("data.attributes.last_analysis_stats.malicious")
    private int maliciousScore;

}
