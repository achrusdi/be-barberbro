package com.enigmacamp.barbershop.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ReviewRequest {
    private String id;

    @JsonProperty("customer_id")
    private String customerId;

    @JsonProperty("barbershop_id")
    private String barbershopId;

    @JsonProperty("rating")
    private Integer rating;

    private String comment;
}