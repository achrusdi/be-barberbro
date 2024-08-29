package com.enigmacamp.barbershop.seeders;

import org.springframework.stereotype.Component;

import com.enigmacamp.barbershop.model.entity.BarberProfilePicture;
import com.enigmacamp.barbershop.repository.BarbersProfilePictureRepository;

import jakarta.annotation.PostConstruct;

@Component
public class BarberProfilePictureSeeder {

    private final BarbersProfilePictureRepository barberProfilePictureRepository;

    public BarberProfilePictureSeeder(BarbersProfilePictureRepository barberProfilePictureRepository) {
        this.barberProfilePictureRepository = barberProfilePictureRepository;
    }

    @PostConstruct
    public void seed() {
        if (!barberProfilePictureRepository.existsById("default1")) {
            BarberProfilePicture barberProfilePicture = BarberProfilePicture.builder()
                    .id("default1")
                    .name("default.jpg")
                    .path("http://localhost:8080/assets/images/barbershop/default.jpg")
                    .size(0L)
                    .contentType("image/jpeg")
                    .createdAt(System.currentTimeMillis())
                    .updatedAt(System.currentTimeMillis())
                    .build();

            barberProfilePictureRepository.saveAndFlush(barberProfilePicture);
        }
    }
}