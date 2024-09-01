package com.enigmacamp.barbershop.service.Impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.enigmacamp.barbershop.model.entity.Review;
import com.enigmacamp.barbershop.repository.ReviewRepository;
import com.enigmacamp.barbershop.service.ReviewService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Review create(Review review) {
        try {
            review.setCreatedAt(System.currentTimeMillis());
            return reviewRepository.save(review);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}