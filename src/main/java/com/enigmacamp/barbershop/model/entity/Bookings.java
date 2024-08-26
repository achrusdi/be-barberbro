package com.enigmacamp.barbershop.model.entity;

import com.enigmacamp.barbershop.constant.BookingStatus;
import jakarta.persistence.*;
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
public class Bookings {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users userId;

    @OneToOne
    @JoinColumn(name = "barber_id", nullable = false)
    private Barbers barberId;

    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    private Sessions sessionId;

    @Column(name = "service_type", nullable = false)
    private String serviceType;

    @OneToOne
    @JoinColumn(name = "payment_id", nullable = false)
    private Payments paymentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BookingStatus status;

    @Column(name = "created_at",nullable = false, updatable = false)
    private Long createdAt;

    @Column(name = "updated_at",nullable = false)
    private Long updatedAt;
}
