package com.enigmacamp.barbershop.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.enigmacamp.barbershop.model.dto.request.BarberNearbyRequest;
import com.enigmacamp.barbershop.model.dto.response.BarberResponse;
import com.enigmacamp.barbershop.model.dto.response.CommonResponse;
import com.enigmacamp.barbershop.model.entity.Barbers;
import com.enigmacamp.barbershop.model.entity.Users;
import com.enigmacamp.barbershop.service.BarberService;
import com.enigmacamp.barbershop.util.JwtHelpers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BarberController {
    private final BarberService barberService;
    private final JwtHelpers jwtHelpers;

    @GetMapping("/barbers")
    public ResponseEntity<CommonResponse<List<BarberResponse>>> getBarbers() {
        try {
            List<BarberResponse> barbers = barberService.getAll();

            // List<BarberResponse> response = new ArrayList<>();

            // if (!barbers.isEmpty()) {
            //     response = barbers.stream().map(barber -> barber.toResponse()).toList();
            // }

            return ResponseEntity.ok(CommonResponse.<List<BarberResponse>>builder()
                    .statusCode(200)
                    .message("Barbers fetched successfully")
                    .data(barbers)
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

    @GetMapping("/barbers/nearby")
    public ResponseEntity<CommonResponse<List<BarberResponse>>> getBarbersNearBy(
            @RequestBody BarberNearbyRequest request) {
        try {
            List<Barbers> barbers = barberService.getByNearBy(request.getLatitude(), request.getLongitude());

            return ResponseEntity.ok(CommonResponse.<List<BarberResponse>>builder()
                    .statusCode(200)
                    .message("Barbers fetched successfully")
                    .data(barbers.stream().map(barber -> barber.toResponse()).toList())
                    .build());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/barbers/current")
    public ResponseEntity<CommonResponse<BarberResponse>> getCurrentBarber(HttpServletRequest srvrequest) {
        try {
            Users user = jwtHelpers.getUser(srvrequest);
            if (user == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }

            Barbers barbers = barberService.getCurrentBarber(user);

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