package com.enigmacamp.jwt.service.Impl;

import com.enigmacamp.jwt.model.entity.User;
import com.enigmacamp.jwt.repository.UserRepository;
import com.enigmacamp.jwt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public User loadUserById(String id){
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("userId not found"));
    }
    @Override
    public User getByContext(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(authentication.getPrincipal().toString())
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public User loadUserByUsername(String username)  {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("username not found"));
    }
}
