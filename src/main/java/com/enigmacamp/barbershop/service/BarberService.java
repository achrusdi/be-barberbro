package com.enigmacamp.barbershop.service;

import com.enigmacamp.barbershop.model.dto.request.BarberRequest;
import com.enigmacamp.barbershop.model.dto.response.BarberResponse;

import jakarta.servlet.http.HttpServletRequest;

public interface BarberService {
    BarberResponse create (HttpServletRequest srvrequest, BarberRequest request);
}
