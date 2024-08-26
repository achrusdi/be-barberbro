package com.enigmacamp.barbershop.service.Impl;

import com.enigmacamp.barbershop.constant.UserRole;
import com.enigmacamp.barbershop.model.dto.request.LoginRequest;
import com.enigmacamp.barbershop.model.dto.request.RegisterRequest;
import com.enigmacamp.barbershop.model.dto.response.LoginResponse;
import com.enigmacamp.barbershop.model.dto.response.RegisterResponse;
import com.enigmacamp.barbershop.model.entity.Role;
import com.enigmacamp.barbershop.model.entity.Users;
import com.enigmacamp.barbershop.repository.UserRepository;
import com.enigmacamp.barbershop.security.JwtService;
import com.enigmacamp.barbershop.service.AuthService;
import com.enigmacamp.barbershop.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RegisterResponse regiserUser(RegisterRequest request) throws DataIntegrityViolationException {
        Role role = roleService.getOrCreate(UserRole.valueOf(request.getRole()));

        Users user = Users.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(List.of(role))
                .createdAt(System.currentTimeMillis())
                .updateAt(System.currentTimeMillis())
                .build();

         userRepository.saveAndFlush(user);

         List<String> roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        return RegisterResponse.builder()
                .email(user.getUsername())
                .roles(roles)
                .build();
    }
    @Transactional(readOnly = true)
    @Override
    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Users user = (Users) authentication.getPrincipal();
        String token = jwtService.generateToken(user);
        return LoginResponse.builder()
                .userId(user.getId())
                .email(user.getUsername())
                .roles(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .token(token)
                .build();
    }
}
