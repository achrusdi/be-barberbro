package com.enigmacamp.jwt.service.Impl;

import com.enigmacamp.jwt.constant.UserRole;
import com.enigmacamp.jwt.model.dto.request.AuthRequest;
import com.enigmacamp.jwt.model.dto.response.LoginResponse;
import com.enigmacamp.jwt.model.dto.response.RegisterResponse;
import com.enigmacamp.jwt.model.entity.Role;
import com.enigmacamp.jwt.model.entity.User;
import com.enigmacamp.jwt.repository.RoleRepository;
import com.enigmacamp.jwt.repository.UserRepository;
import com.enigmacamp.jwt.security.JwtService;
import com.enigmacamp.jwt.service.AuthService;
import com.enigmacamp.jwt.service.RoleService;
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
    public RegisterResponse regiserUser(AuthRequest request) throws DataIntegrityViolationException {
        Role role = roleService.getOrCreate(UserRole.CUSTOMER);

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(List.of(role))
                .isEnable(true)
                .build();

         userRepository.saveAndFlush(user);

         List<String> roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        return RegisterResponse.builder()
                .username(user.getUsername())
                .roles(roles)
                .build();
    }
    @Transactional(readOnly = true)
    @Override
    public LoginResponse login(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();
        String token = jwtService.generateToken(user);
        return LoginResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .roles(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .token(token)
                .build();
    }
}
