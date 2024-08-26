package com.enigmacamp.jwt.service;

import com.enigmacamp.jwt.constant.UserRole;
import com.enigmacamp.jwt.model.entity.Role;

public interface RoleService {
    Role getOrCreate(UserRole role);
}
