package com.enigmacamp.barbershop.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.enigmacamp.barbershop.constant.BookingStatus;
import com.enigmacamp.barbershop.model.dto.request.ReviewRequest;
import com.enigmacamp.barbershop.model.dto.response.CommonResponse;
import com.enigmacamp.barbershop.model.dto.response.ReviewResponse;
import com.enigmacamp.barbershop.model.entity.Barbers;
import com.enigmacamp.barbershop.model.entity.Booking;
import com.enigmacamp.barbershop.model.entity.Customer;
import com.enigmacamp.barbershop.model.entity.Review;
import com.enigmacamp.barbershop.model.entity.Users;
import com.enigmacamp.barbershop.service.BarberService;
import com.enigmacamp.barbershop.service.BookingService;
import com.enigmacamp.barbershop.service.CustomerService;
import com.enigmacamp.barbershop.service.ReviewService;
import com.enigmacamp.barbershop.util.JwtHelpers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {
    private final ReviewService reviewService;
    private final JwtHelpers jwtHelpers;
    private final CustomerService customerService;
    private final BarberService barberService;
    private final BookingService bookingService;

    @PostMapping("/reviews")
    public ResponseEntity<CommonResponse<ReviewResponse>> createReview(@RequestBody ReviewRequest request,
            HttpServletRequest srvrequest) {

        Users user = jwtHelpers.getUser(srvrequest);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        Customer customer = customerService.getByUserId(user);

        if (customer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
        }

        Barbers barber = barberService.getById(request.getBarbershopId());

        if (barber == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Barber not found");
        }

        if (request.getRating() < 1 || request.getRating() > 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rating must be between 1 and 5");
        }

        Booking booking = bookingService.getById(request.getBookingId());

        if (booking == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found");
        }

        if (booking.getStatus() == null ? BookingStatus.Completed.name() != null : !booking.getStatus().equals(BookingStatus.Completed.name())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Booking must be completed before creating review");
        }

        Review review = Review.builder()
                .customerId(customer)
                .barbershopId(barber)
                .bookingId(booking)
                .rating(request.getRating())
                .comment(request.getComment())
                .build();

        review = reviewService.create(review);

        if (review == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create review");
        }

        return ResponseEntity.ok(CommonResponse.<ReviewResponse>builder()
                .statusCode(200)
                .message("Review created")
                .data(review.toResponse())
                .build());
    }

    // @PostMapping("/reviews")
    // public ResponseEntity<CommonResponse<ReviewResponse>>
    // createReview(@RequestBody ReviewRequest request,
    // HttpServletRequest srvrequest) {

    // // Users user = jwtHelpers.getUser(srvrequest);

    // // if (user == null) {
    // // throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
    // "Unauthorized");
    // // }

    // // Customer customer = customerService.getByUserId(user);

    // // Review review = Review.builder()
    // // .customerId(customer)
    // // .rating(request.getRating())
    // // .comment(request.getComment())
    // // .build();

    // // review = reviewService.create(review);

    // // if (review == null) {
    // // throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
    // "Failed to update review");
    // // }

    // // return ResponseEntity.ok(CommonResponse.<ReviewResponse>builder()
    // // .statusCode(200)
    // // .message("Review updated")
    // // .data(review.toResponse())
    // // .build());

    // return null;
    // }
}