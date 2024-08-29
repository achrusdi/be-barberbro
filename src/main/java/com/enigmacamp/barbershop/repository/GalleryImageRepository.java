package com.enigmacamp.barbershop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.enigmacamp.barbershop.model.entity.GalleryImage;

public interface GalleryImageRepository extends JpaRepository<GalleryImage, String> {
}