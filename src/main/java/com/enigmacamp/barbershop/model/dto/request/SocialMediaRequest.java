package com.enigmacamp.barbershop.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SocialMediaRequest {
    @JsonProperty("platform_name")
    private String platform_name;
    @JsonProperty("platform_url")
    private String platform_url;
}