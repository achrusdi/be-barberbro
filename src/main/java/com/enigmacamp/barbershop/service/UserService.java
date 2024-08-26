package com.enigmacamp.barbershop.service;

import com.enigmacamp.barbershop.model.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User loadUserByUsername(String username);
    User loadUserById(String id);
    User getByContext();

}
