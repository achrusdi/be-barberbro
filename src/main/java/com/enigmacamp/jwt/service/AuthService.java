package com.enigmacamp.jwt.service;

import com.enigmacamp.jwt.model.dto.request.AuthRequest;
import com.enigmacamp.jwt.model.dto.response.LoginResponse;
import com.enigmacamp.jwt.model.dto.response.RegisterResponse;
import com.enigmacamp.jwt.model.entity.User;

public interface AuthService {
    RegisterResponse regiserUser(AuthRequest request);
    LoginResponse login(AuthRequest request);
}
