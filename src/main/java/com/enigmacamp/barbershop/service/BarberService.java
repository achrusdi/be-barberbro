package com.enigmacamp.barbershop.service;

import java.util.List;

import com.enigmacamp.barbershop.model.dto.request.BarberRequest;
import com.enigmacamp.barbershop.model.dto.response.BarberResponse;
import com.enigmacamp.barbershop.model.entity.Barbers;
import com.enigmacamp.barbershop.model.entity.Users;

import jakarta.servlet.http.HttpServletRequest;

public interface BarberService {
    BarberResponse create(HttpServletRequest srvrequest, BarberRequest request);

    Barbers update(HttpServletRequest srvrequest, BarberRequest request);

    Barbers update(Barbers barbers);

    Barbers getByEmail(String email);

    List<BarberResponse> getAll();

    Barbers getById(String id);

    Barbers getByUserId(Users user);

    List<Barbers> getByNearBy(double latitude, double longitude);

    Barbers getCurrentBarber(Users user);
}
