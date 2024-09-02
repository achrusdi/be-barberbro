package com.enigmacamp.barbershop.service;

import java.util.List;

import com.enigmacamp.barbershop.model.entity.Barbers;
import com.enigmacamp.barbershop.model.entity.Booking;
import com.enigmacamp.barbershop.model.entity.Customer;

public interface BookingService {
    Booking create(Booking booking);

    Booking getById(String bookingId);

    Booking update(Booking booking);

    List<Booking> getByCustomer(Customer customer);

    List<Booking> getByBarber(Barbers barber);

    Booking cancel(Barbers barber, String bookingId);

    Booking completed(Barbers barber, String bookingId);
}