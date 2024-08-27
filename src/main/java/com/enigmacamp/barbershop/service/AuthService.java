package com.enigmacamp.barbershop.service;

import com.enigmacamp.barbershop.model.dto.request.LoginRequest;
import com.enigmacamp.barbershop.model.dto.request.RegisterRequest;
import com.enigmacamp.barbershop.model.dto.response.LoginResponse;
import com.enigmacamp.barbershop.model.dto.response.RegisterResponse;

public interface AuthService {
    RegisterResponse regiserUser(RegisterRequest request);
    LoginResponse login(LoginRequest request);
}
