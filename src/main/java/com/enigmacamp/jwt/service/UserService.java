package com.enigmacamp.jwt.service;

import com.enigmacamp.jwt.model.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User loadUserByUsername(String username);
    User loadUserById(String id);
    User getByContext();

}
