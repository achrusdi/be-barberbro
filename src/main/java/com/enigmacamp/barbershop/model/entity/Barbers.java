package com.enigmacamp.barbershop.model.entity;

import com.enigmacamp.barbershop.constant.BarberStatus;
import com.enigmacamp.barbershop.model.dto.response.BarberResponse;

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
@Table(name = "m_barbers")
public class Barbers {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "lat_location", nullable = false)
    private Double latitudeLocation;

    @Column(name = "long_location", nullable = false)
    private Double longitudeLocation;

    @Column(name = "description")
    private String description;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users userId;

    @Column(name = "balance", nullable = false)
    private float balance;

    @Column(name = "verified", nullable = false)
    private Boolean verified;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private BarberStatus status;

    @OneToOne
    @JoinColumn(name = "id_barbers_avatar")
    private BarbersAvatar barbersAvatar;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Long createdAt;

    @Column(name = "updated_at", nullable = false)
    private Long updateAt;

    public BarberResponse toResponse() {
        return BarberResponse.builder()
                .barberId(id)
                .build();
    }

}