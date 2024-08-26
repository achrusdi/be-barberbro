package com.enigmacamp.jwt.repository;

import com.enigmacamp.jwt.constant.UserRole;
import com.enigmacamp.jwt.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByRole(UserRole role);

}
