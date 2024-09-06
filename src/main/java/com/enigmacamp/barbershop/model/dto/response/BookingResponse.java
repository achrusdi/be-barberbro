package com.enigmacamp.barbershop.model.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookingResponse {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String booking_id;
    
    private CustomerResponse customer;

    private BarberResponse barber;

    private List<ServiceResponse> services;

    private Long bookingDate;

    private String bookingTime;

    private String status;

    private String midtransPaymentUrl;

    private Double totalPrice;

    private Long createdAt;

    private Long updatedAt;
}