package com.example.demo.DTO;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Map;

@Data
@RequiredArgsConstructor
@ToString
public class BlockerAttribute {

    @SerializedName("last_analysis_stats")
    private Map<String, Integer> domainStat;

}