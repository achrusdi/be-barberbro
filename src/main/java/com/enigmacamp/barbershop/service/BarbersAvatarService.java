package com.enigmacamp.barbershop.service;

import com.enigmacamp.barbershop.model.entity.BarbersAvatar;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface BarbersAvatarService {
    BarbersAvatar create(MultipartFile multipartFile);
    Resource getById(String id);
    void deleteById(String id);
}
