package com.enigmacamp.barbershop.controller;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.enigmacamp.barbershop.constant.BookingStatus;
import com.enigmacamp.barbershop.constant.ResponseMessage;
import com.enigmacamp.barbershop.model.dto.request.BookingRequest;
import com.enigmacamp.barbershop.model.dto.request.MidtransWebhookRequest;
import com.enigmacamp.barbershop.model.dto.response.BookingResponse;
import com.enigmacamp.barbershop.model.dto.response.CommonResponse;
import com.enigmacamp.barbershop.model.entity.Barbers;
import com.enigmacamp.barbershop.model.entity.Booking;
import com.enigmacamp.barbershop.model.entity.Customer;
import com.enigmacamp.barbershop.model.entity.OperationalHour;
import com.enigmacamp.barbershop.model.entity.Service;
import com.enigmacamp.barbershop.model.entity.Users;
import com.enigmacamp.barbershop.service.BarberService;
import com.enigmacamp.barbershop.service.BookingService;
import com.enigmacamp.barbershop.service.CustomerService;
import com.enigmacamp.barbershop.service.ServiceService;
import com.enigmacamp.barbershop.util.JwtHelpers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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

    private static final Pattern TIME_PATTERN = Pattern.compile("^([01]\\d|2[0-3]):([0]\\d)$");

    @PostMapping("/bookings")
    public ResponseEntity<CommonResponse<BookingResponse>> createBooking(@Valid @RequestBody BookingRequest booking,
            HttpServletRequest srvrequest) {

        if (!isTimestampAfterToday(booking.getBookingDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Booking date must be in the future");
        }

        Users user = jwtHelpers.getUser(srvrequest);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        Barbers barber = barberService.getBarberById(booking.getBarber_id());

        if (barber == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND);
        }

        if (booking.getServices() == null || booking.getServices().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Services cannot be empty");
        }

        LocalDateTime dateTime = Instant.ofEpochMilli(booking.getBookingDate())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        String dayBooking = dateTime.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault()).toUpperCase();

        if (!isValidTimeFormat(booking.getBookingTime())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid time format, must be in HH:mm format");
        }

        Barbers barbers = barberService.getBarberById(booking.getBarber_id());

        if (barbers == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND);
        }

        OperationalHour oh = barbers.getOperationalHours().stream()
                .filter(o -> o.getDay().equals(dayBooking)).findFirst().orElse(null);

        if (oh == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Barber is not available on this day");
        }

        if (!isTimeInRange(booking.getBookingTime(), oh.getOpening_time().toString(),
                oh.getClosing_time().toString())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Barber is not available at this time");
        }

        List<Booking> bookings = bookingService.getAllByBarberAndDate(barbers, booking.getBookingDate());
        List<Booking> filteredBookings = new ArrayList<>();

        if (bookings != null && !bookings.isEmpty()) {
            filteredBookings = bookings.stream()
                    .filter(b -> booking.getBookingTime().equals(b.getBookingTime().toString())).toList();
        }

        if (filteredBookings != null && !filteredBookings.isEmpty()
                && filteredBookings.size() >= oh.getLimitPerSession()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Barber session is not available at this time");
        }

        Customer customer = customerService.getByUserId(user);

        if (customer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    ResponseMessage.ERROR_NOT_FOUND);
        }

        List<Service> services = new ArrayList<>();

        for (String serviceId : booking.getServices()) {
            Service service = serviceService.getById(serviceId);
            if (service == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Service not found");
            }
            services.add(service);
        }

        Booking bookingToBeCreated = Booking.builder()
                .customerId(customer)
                .barberId(barber)
                .services(services)
                .bookingDate(booking.getBookingDate())
                // .bookingTime(LocalTime.parse(booking.getBookingTime()))
                .bookingTime(LocalTime.parse(booking.getBookingTime()))
                .status(BookingStatus.Pending.name())
                .createdAt(System.currentTimeMillis())
                .updatedAt(System.currentTimeMillis())
                .build();

        bookingToBeCreated = bookingService.create(bookingToBeCreated);

        System.out.println("=========================================");
        System.out.println("Booking created: " + bookingToBeCreated);
        System.out.println("=========================================");

        if (bookingToBeCreated == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request");
        }

        return ResponseEntity.ok(CommonResponse.<BookingResponse>builder()
                .statusCode(201)
                .message("Booking created successfully")
                .data(bookingToBeCreated.toResponse())
                .build());
    }

    @GetMapping("/bookings/current")
    public ResponseEntity<CommonResponse<List<BookingResponse>>> getCurrentBooking(HttpServletRequest srvrequest) {
        Users user = jwtHelpers.getUser(srvrequest);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        Customer customer = customerService.getByUserId(user);
        Barbers barber = barberService.getByUserId(user);

        List<Booking> bookings = new ArrayList<>();

        if (customer != null) {
            bookings = bookingService.getByCustomer(customer);
        }

        if (barber != null) {
            bookings = bookingService.getByBarber(barber);
        }

        return ResponseEntity.ok(CommonResponse.<List<BookingResponse>>builder()
                .statusCode(200)
                .message("List of booking")
                .data(bookings.stream().map(Booking::toResponse).toList())
                .build());
    }

    @PutMapping("/bookings/{id}/cancel")
    public ResponseEntity<CommonResponse<BookingResponse>> cancelBooking(HttpServletRequest srvrequest,
            @PathVariable String id) {

        Users user = jwtHelpers.getUser(srvrequest);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        Barbers barber = barberService.getByUserId(user);

        if (barber == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND);
        }

        Booking booking = bookingService.cancel(barber, id);

        if (booking == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND);
        }

        return ResponseEntity.ok(CommonResponse.<BookingResponse>builder()
                .statusCode(200)
                .message("Booking canceled")
                .data(booking.toResponse())
                .build());
    }

    @PutMapping("/bookings/{id}/complete")
    public ResponseEntity<CommonResponse<BookingResponse>> completeBooking(HttpServletRequest srvrequest,
            @PathVariable String id) {
        Users user = jwtHelpers.getUser(srvrequest);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        Barbers barber = barberService.getByUserId(user);

        if (barber == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND);
        }

        Booking booking = bookingService.completed(barber, id);

        if (booking == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND);
        }

        return ResponseEntity.ok(CommonResponse.<BookingResponse>builder()
                .statusCode(200)
                .message("Booking completed")
                .data(booking.toResponse())
                .build());
    }

    @GetMapping("/bookings")
    public ResponseEntity<CommonResponse<List<BookingResponse>>> getAllBooking() {
        try {

            List<Booking> bookings = bookingService.getAll();
            return ResponseEntity.ok(CommonResponse.<List<BookingResponse>>builder()
                    .statusCode(200)
                    .message("List of booking")
                    .data(bookings.stream().map(Booking::toResponse).toList())
                    .build());

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("bookings/webhook")
    public void webhook(@RequestBody MidtransWebhookRequest request) {
        bookingService.bookingWebhook(request);
    }

    @GetMapping("/bookings/{id}/update")
    public ResponseEntity<CommonResponse<BookingResponse>> updateBooking(@PathVariable String id) {

        Booking booking = bookingService.getById(id);

        if (booking == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND);
        }

        booking = bookingService.updateBookingStatus(booking);

        if (booking == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update booking");
        }

        return ResponseEntity.ok(CommonResponse.<BookingResponse>builder()
                .statusCode(200)
                .message("Booking updated successfully")
                .data(booking.toResponse())
                .build());
    }

    private Boolean isValidTimeFormat(String time) {
        return TIME_PATTERN.matcher(time).matches();
    }

    private Boolean isTimeInRange(String time, String startTime, String endTime) {
        try {
            LocalTime timeToCheck = LocalTime.parse(time);
            LocalTime start = LocalTime.parse(startTime);
            LocalTime end = LocalTime.parse(endTime);

            return !timeToCheck.isBefore(start) && !timeToCheck.isAfter(end);
        } catch (Exception e) {
            return false;
        }

    }

    private Boolean isTimestampAfterToday(Long timestamp) {
        LocalDate today = LocalDate.now();
        ZonedDateTime startOfToday = today.atStartOfDay(ZoneId.systemDefault());
        long startOfTodayTimestamp = startOfToday.toInstant().toEpochMilli();

        return timestamp > startOfTodayTimestamp;
    }

}