package com.enigmacamp.barbershop.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enigmacamp.barbershop.model.dto.request.BarberRequest;
import com.enigmacamp.barbershop.model.dto.response.BarberResponse;
import com.enigmacamp.barbershop.model.dto.response.CommonResponse;
import com.enigmacamp.barbershop.service.BarberService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BarberController {
    private final BarberService barberService;

    @PostMapping("/barber")
    public ResponseEntity<CommonResponse<BarberResponse>> create(@RequestBody BarberRequest request,
            HttpServletRequest srvrequest) {
        BarberResponse response = barberService.create(srvrequest, request);

        if (response == null) {
            return null;
        }

        return ResponseEntity.ok(CommonResponse.<BarberResponse>builder()
                .statusCode(201)
                .message("Barber created successfully")
                .data(response)
                .build());
    }
}