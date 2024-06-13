package com.example.demo.DTO;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@RequiredArgsConstructor
@ToString
public class BlockerData {

    @SerializedName("id")
    private String domainName;
    @SerializedName("attributes")
    private BlockerAttribute domainAttribute;

}
