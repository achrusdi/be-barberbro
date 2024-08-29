package com.enigmacamp.barbershop.model.entity;

import java.time.LocalTime;
import java.util.List;

import com.enigmacamp.barbershop.model.dto.response.BookingResponse;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "m_bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String booking_id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customerId;

    @ManyToOne
    @JoinColumn(name = "barber_id", nullable = false)
    private Barbers barberId;

    @ManyToMany
    @JoinTable(name = "m_booking_services", joinColumns = @JoinColumn(name = "booking_id"), inverseJoinColumns = @JoinColumn(name = "service_id"))
    private List<Service> services;

    @Column(name = "booking_date", nullable = false)
    private Long bookingDate;

    @Column(name = "booking_time", nullable = false)
    private LocalTime bookingTime;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "total_price", nullable = true)
    private Double totalPrice;

    @Column(name = "created_at", nullable = false)
    private Long createdAt;

    @Column(name = "updated_at", nullable = false)
    private Long updatedAt;

    public BookingResponse toResponse() {
        return BookingResponse.builder()
                .customer_id(customerId.getId())
                .barber_id(barberId.getId())
                .services(services.stream().map(service -> service.toResponse()).toList())
                .bookingDate(bookingDate)
                .bookingTime(bookingTime.toString())
                .status(status)
                .totalPrice(totalPrice)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }
}