package com.enigmacamp.barbershop.controller;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.enigmacamp.barbershop.constant.ResponseMessage;
import com.enigmacamp.barbershop.model.dto.request.BookingRequest;
import com.enigmacamp.barbershop.model.dto.response.BookingResponse;
import com.enigmacamp.barbershop.model.dto.response.CommonResponse;
import com.enigmacamp.barbershop.model.entity.Barbers;
import com.enigmacamp.barbershop.model.entity.Booking;
import com.enigmacamp.barbershop.model.entity.Customer;
import com.enigmacamp.barbershop.model.entity.Service;
import com.enigmacamp.barbershop.model.entity.Users;
import com.enigmacamp.barbershop.service.BarberService;
import com.enigmacamp.barbershop.service.BookingService;
import com.enigmacamp.barbershop.service.CustomerService;
import com.enigmacamp.barbershop.service.ServiceService;
import com.enigmacamp.barbershop.util.JwtHelpers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BookingController {
    private final BookingService bookingService;
    private final BarberService barberService;
    private final CustomerService customerService;
    private final ServiceService serviceService;
    private final JwtHelpers jwtHelpers;

    @PostMapping("/bookings")
    public ResponseEntity<CommonResponse<BookingResponse>> createBooking(@RequestBody BookingRequest booking,
            HttpServletRequest srvrequest) {
        Users user = jwtHelpers.getUser(srvrequest);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        Barbers barber = barberService.getById(booking.getBarber_id());

        if (barber == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND);
        }

        Customer customer = customerService.getByUserId(user);

        System.out.println("user " + user);
        System.out.println("customer " + customer);

        if (customer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    ResponseMessage.ERROR_NOT_FOUND);
        }

        List<Service> services = new ArrayList<>();

        for (String serviceId : booking.getServices()) {
            Service service = serviceService.getById(serviceId);
            services.add(service);
        }

        Booking bookingToBeCreated = Booking.builder()
                .customerId(customer)
                .barberId(barber)
                .services(services)
                .bookingDate(booking.getBookingDate())
                // .bookingTime(LocalTime.parse(booking.getBookingTime()))
                .bookingTime(LocalTime.parse(booking.getBookingTime()))
                .status(booking.getStatus().name())
                .createdAt(System.currentTimeMillis())
                .updatedAt(System.currentTimeMillis())
                .build();

        bookingToBeCreated = bookingService.create(bookingToBeCreated);
        // System.out.println("bookingToBeCreated " + bookingToBeCreated);
        // System.out.println("bookingToBeCreated " + bookingToBeCreated);
        // System.out.println("Booking time " + booking.getBookingTime());
        if (bookingToBeCreated == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request");
        }

        return ResponseEntity.ok(CommonResponse.<BookingResponse>builder()
                .statusCode(201)
                .message("Booking created successfully")
                .data(bookingToBeCreated.toResponse())
                .build());

    }
}