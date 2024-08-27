package com.enigmacamp.barbershop.service;

import com.enigmacamp.barbershop.model.entity.Portfolio;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface PortfolioService {
    Portfolio create(HttpServletRequest request, MultipartFile multipartFile);
    Resource getById(String id);
    void deleteById(String id);
}
