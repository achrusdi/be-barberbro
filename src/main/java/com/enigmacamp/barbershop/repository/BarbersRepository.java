package com.enigmacamp.barbershop.repository;

import java.util.Optional;

import com.enigmacamp.barbershop.model.entity.Barbers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.enigmacamp.barbershop.model.entity.Users;

@Repository
public interface BarbersRepository extends JpaRepository<Barbers, String> {
    Barbers findByEmail(String email);

    Optional<Barbers> findByUserId(Users user);
}
