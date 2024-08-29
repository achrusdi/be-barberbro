package com.enigmacamp.barbershop.service;

import com.enigmacamp.barbershop.model.entity.OperationalHour;

import jakarta.servlet.http.HttpServletRequest;

public interface OperationalHourService {
    OperationalHour create(HttpServletRequest srvrequest, OperationalHour request);
}