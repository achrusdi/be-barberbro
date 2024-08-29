package com.enigmacamp.barbershop.service;

import java.util.List;

import com.enigmacamp.barbershop.model.dto.request.BarberRequest;
import com.enigmacamp.barbershop.model.dto.response.BarberResponse;
import com.enigmacamp.barbershop.model.entity.Barbers;

import jakarta.servlet.http.HttpServletRequest;

public interface BarberService {
    BarberResponse create(HttpServletRequest srvrequest, BarberRequest request);

    Barbers update(HttpServletRequest srvrequest, BarberRequest request);

    Barbers getByEmail(String email);

    List<Barbers> getAll();

    Barbers getById(String id);
}
