package com.enigmacamp.jwt.service.Impl;

import com.enigmacamp.jwt.constant.UserRole;
import com.enigmacamp.jwt.model.entity.Role;
import com.enigmacamp.jwt.repository.RoleRepository;
import com.enigmacamp.jwt.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
private final RoleRepository roleRepository;
    @Override
    public Role getOrCreate(UserRole role) {
        Optional<Role> optionalRole = roleRepository.findByRole(role);
        if (optionalRole.isPresent()) {
            return optionalRole.get();
        }

        Role currentRole = Role.builder().role(role).build();
        return roleRepository.saveAndFlush(currentRole);
    }
}
