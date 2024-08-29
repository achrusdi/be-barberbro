package com.enigmacamp.barbershop.service.Impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.enigmacamp.barbershop.model.entity.Booking;
import com.enigmacamp.barbershop.repository.BookingRepository;
import com.enigmacamp.barbershop.service.BookingService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Booking create(Booking booking) {
        try {
            
            return bookingRepository.save(booking);
            // System.out.println("booking " + booking);
            // return null;
        } catch (Exception e) {
            throw new RuntimeException("Error breh " + e);
        }
    }
}