package com.enigmacamp.barbershop.service.Impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.enigmacamp.barbershop.constant.BookingStatus;
import com.enigmacamp.barbershop.model.entity.Barbers;
import com.enigmacamp.barbershop.model.entity.Booking;
import com.enigmacamp.barbershop.model.entity.Customer;
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
            List<com.enigmacamp.barbershop.model.entity.Service> services = booking.getServices();

            Double totalPrice = services.stream().mapToDouble(com.enigmacamp.barbershop.model.entity.Service::getPrice)
                    .sum();

            booking.setTotalPrice(totalPrice);
            return bookingRepository.save(booking);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Booking getById(String bookingId) {
        try {
            return bookingRepository.findById(bookingId).orElse(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Booking update(Booking booking) {
        try {
            return bookingRepository.save(booking);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Booking> getByCustomer(Customer customer) {
        try {
            return bookingRepository.findByCustomerId(customer).orElse(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Booking> getByBarber(Barbers barber) {
        try {
            return bookingRepository.findByBarberId(barber).orElse(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Booking cancel(Barbers barbers, String bookingId) {
        try {
            Booking booking = bookingRepository.findByBarberIdAndBookingId(barbers, bookingId).orElse(null);

            if (booking == null) {
                throw new RuntimeException();
            }

            booking.setStatus(BookingStatus.Canceled.name());

            return bookingRepository.save(booking);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Booking completed(Barbers barbers, String bookingId) {
        try {
            Booking booking = bookingRepository.findByBarberIdAndBookingId(barbers, bookingId).orElse(null);

            if (booking == null) {
                throw new RuntimeException();
            }

            booking.setStatus(BookingStatus.Completed.name());

            return bookingRepository.save(booking);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Booking> getAll() {
        return bookingRepository.findAll();
    }
}