package com.enigmacamp.jwt.controller;

import com.enigmacamp.jwt.model.dto.request.AuthRequest;
import com.enigmacamp.jwt.model.dto.response.CommonResponse;
import com.enigmacamp.jwt.model.dto.response.LoginResponse;
import com.enigmacamp.jwt.model.dto.response.RegisterResponse;
import com.enigmacamp.jwt.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private static final Logger logger = LogManager.getLogger(AuthService.class);

    @PostMapping("/register")
    public ResponseEntity<CommonResponse<RegisterResponse>> registerUser(@RequestBody AuthRequest request) {
        logger.info("Accessed Endpoint : " + "/register");

        RegisterResponse response = authService.regiserUser(request);
        return ResponseEntity.ok(CommonResponse.<RegisterResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Successfully registered new account")
                .data(response)
                .build());
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponse<LoginResponse>> login(@RequestBody AuthRequest request) {
        logger.info("Accessed Endpoint : " + "/login");

        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(CommonResponse.<LoginResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Login successful")
                .data(response)
                .build());
    }
}
