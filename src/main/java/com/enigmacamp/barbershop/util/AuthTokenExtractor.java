package com.enigmacamp.barbershop.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;

public class AuthTokenExtractor {


    public String getTokenFromHeader(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7); // Menghapus "Bearer " dari token
            DecodedJWT jwt = JWT.decode(token);

            return jwt.getClaim("userId").asString();
        }
        return null;
    }

}
