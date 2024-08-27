package com.enigmacamp.barbershop.model.dto.request;

import lombok.Data;

@Data
public class BarberRequest {
    private String name;
    private Double latitude;
    private Double longitude;
    private String address;
    private String description;
    private String portofolioImages;
}
