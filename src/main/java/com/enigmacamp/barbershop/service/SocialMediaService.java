package com.enigmacamp.barbershop.service;

import com.enigmacamp.barbershop.model.entity.SocialMedia;

import jakarta.servlet.http.HttpServletRequest;

public interface SocialMediaService {
    SocialMedia create(HttpServletRequest srvrequest, SocialMedia request);
}