package com.enigmacamp.barbershop.service;

import com.enigmacamp.barbershop.model.dto.request.BarberRequest;
import com.enigmacamp.barbershop.model.dto.response.BarberResponse;

public interface BarberService {
    BarberResponse create (BarberRequest request);   
}
