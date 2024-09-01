package com.enigmacamp.barbershop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.enigmacamp.barbershop.model.entity.Barbers;
import com.enigmacamp.barbershop.model.entity.Booking;
import com.enigmacamp.barbershop.model.entity.Customer;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {
    Optional<List<Booking>> findByCustomerId(Customer customer);

    Optional<List<Booking>> findByBarberId(Barbers barber);

    Optional<Booking> findByBarberIdAndBookingId(Barbers barber, String bookingId);
}