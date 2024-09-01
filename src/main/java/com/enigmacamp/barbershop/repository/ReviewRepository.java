package com.enigmacamp.barbershop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.enigmacamp.barbershop.model.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, String>{
}