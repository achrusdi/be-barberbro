package com.enigmacamp.barbershop.repository;

import com.enigmacamp.barbershop.model.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortofolioRepository extends JpaRepository<Portfolio, String> {
}
