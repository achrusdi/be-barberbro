package com.enigmacamp.barbershop.repository;

import com.enigmacamp.barbershop.model.entity.Barbers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BarbersRepository extends JpaRepository<Barbers, String> {
    Barbers findByEmail(String email);
}
