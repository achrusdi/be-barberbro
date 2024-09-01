package com.enigmacamp.barbershop.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.enigmacamp.barbershop.model.dto.response.BarberResponse;
import com.enigmacamp.barbershop.model.dto.response.CommonResponse;
import com.enigmacamp.barbershop.model.entity.Barbers;
import com.enigmacamp.barbershop.service.BarberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BarberController {
    private final BarberService barberService;

    @GetMapping("/barbers")
    public ResponseEntity<CommonResponse<List<BarberResponse>>> getBarbers() {
        try {
            List<Barbers> barbers = barberService.getAll();

            List<BarberResponse> response = new ArrayList<>();

            if (!barbers.isEmpty()) {
                response = barbers.stream().map(barber -> barber.toResponse()).toList();
            }

            return ResponseEntity.ok(CommonResponse.<List<BarberResponse>>builder()
                    .statusCode(200)
                    .message("Barbers fetched successfully")
                    .data(response)
                    .build());

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    @GetMapping("/barbers/{id}")
    public ResponseEntity<CommonResponse<BarberResponse>> getBarberById(@PathVariable String id) {
        try {
            Barbers barbers = barberService.getById(id);
            if (barbers == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Barber not found");
            }
            return ResponseEntity.ok(CommonResponse.<BarberResponse>builder()
                    .statusCode(200)
                    .message("Barber fetched successfully")
                    .data(barbers.toResponse())
                    .build());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}