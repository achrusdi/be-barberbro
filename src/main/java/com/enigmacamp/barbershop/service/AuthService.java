package com.enigmacamp.barbershop.service;

import com.enigmacamp.barbershop.model.dto.request.AuthRequest;
import com.enigmacamp.barbershop.model.dto.response.LoginResponse;
import com.enigmacamp.barbershop.model.dto.response.RegisterResponse;

public interface AuthService {
    RegisterResponse regiserUser(AuthRequest request);
    LoginResponse login(AuthRequest request);
}
