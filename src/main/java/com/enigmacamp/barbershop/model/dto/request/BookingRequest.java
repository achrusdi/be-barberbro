package com.enigmacamp.barbershop.model.dto.request;

import java.util.List;

import com.enigmacamp.barbershop.constant.BookingStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class BookingRequest {
    private String barber_id;

    private List<String> services;

    @JsonProperty("booking_date")
    private Long bookingDate;

    @JsonProperty("booking_time")
    private String bookingTime;

    private BookingStatus status;

    @JsonProperty("total_price")
    private Double totalPrice;
}