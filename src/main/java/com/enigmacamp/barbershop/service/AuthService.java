package com.enigmacamp.barbershop.service;

import com.enigmacamp.barbershop.model.dto.request.BarberRegisterRequest;
import com.enigmacamp.barbershop.model.dto.request.LoginRequest;
import com.enigmacamp.barbershop.model.dto.request.RegisterRequest;
import com.enigmacamp.barbershop.model.dto.response.BarberRegisterResponse;
import com.enigmacamp.barbershop.model.dto.response.LoginResponse;
import com.enigmacamp.barbershop.model.dto.response.RegisterResponse;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    RegisterResponse registerCustomer(RegisterRequest request, HttpServletRequest srvrequest);

    BarberRegisterResponse registerBarber(BarberRegisterRequest request, HttpServletRequest srvrequest);

    LoginResponse login(LoginRequest request);
}
