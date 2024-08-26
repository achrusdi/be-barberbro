package com.enigmacamp.barbershop.service;

import com.enigmacamp.barbershop.model.entity.Users;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    Users loadUserByUsername(String username);
    Users loadUserById(String id);
    Users getByContext();

}
