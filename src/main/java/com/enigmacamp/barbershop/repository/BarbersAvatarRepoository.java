package com.enigmacamp.barbershop.repository;

import com.enigmacamp.barbershop.model.entity.BarbersAvatar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BarbersAvatarRepoository extends JpaRepository<BarbersAvatar,String> {
}
