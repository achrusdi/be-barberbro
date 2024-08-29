package com.enigmacamp.barbershop.service.Impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.enigmacamp.barbershop.model.entity.SocialMedia;
import com.enigmacamp.barbershop.model.entity.Users;
import com.enigmacamp.barbershop.repository.SocialMediaRepository;
import com.enigmacamp.barbershop.service.SocialMediaService;
import com.enigmacamp.barbershop.util.JwtHelpers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SocialMediaServiceImpl implements SocialMediaService {
    private final JwtHelpers jwtHelpers;
    private final SocialMediaRepository socialMediaRepository;

    public SocialMedia create(HttpServletRequest srvrequest, SocialMedia request) {
        try {
            Users user = jwtHelpers.getUser(srvrequest);
            if (user == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }

            return socialMediaRepository.save(request);

        } catch (Exception e) {
            // TODO: handle exception
            throw new RuntimeException(e);
        }
    }
}