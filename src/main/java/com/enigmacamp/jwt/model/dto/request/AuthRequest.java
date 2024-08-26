package com.enigmacamp.jwt.model.dto.request;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}
