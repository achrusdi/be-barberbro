package com.enigmacamp.barbershop.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "m_barber_avatar")
public class BarberAvatar {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "barber_id", nullable = false)
    private Barbers barberId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "path_url")
    private String imageUrl;

    @Column(name = "created_at", nullable = false,updatable = false)
    private Long createdAt;

    @Column(name = "updated_at", nullable = false)
    private Long updatedAt;
}
