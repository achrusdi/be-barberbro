package com.enigmacamp.barbershop.model.dto.request;

import lombok.Data;

@Data
public class ServiceRequest {
    private String service_name;
    private Double price;
}