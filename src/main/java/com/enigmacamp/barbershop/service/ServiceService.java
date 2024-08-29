package com.enigmacamp.barbershop.service;

import com.enigmacamp.barbershop.model.entity.Service;

import jakarta.servlet.http.HttpServletRequest;

public interface ServiceService {
    Service create(HttpServletRequest srvrequest, Service service);
}