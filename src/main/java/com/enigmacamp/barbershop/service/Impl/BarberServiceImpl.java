package com.enigmacamp.barbershop.service.Impl;

import com.enigmacamp.barbershop.model.dto.request.BarberRequest;
import com.enigmacamp.barbershop.model.dto.response.BarberResponse;
import com.enigmacamp.barbershop.model.entity.Barbers;
import com.enigmacamp.barbershop.repository.BarbersRepository;
import com.enigmacamp.barbershop.repository.PortfolioRepository;
import com.enigmacamp.barbershop.service.BarberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BarberServiceImpl implements BarberService {
    private final BarbersRepository barbersRepository;
    private final PortfolioRepository portfolioRepository;

    @Override
    public BarberResponse create(BarberRequest response) {
//        profilePath
//        Barbers.builder()
//                .name(response.getName())
//                .profilePictureUrl(response.getProfilePictureUrl() ? response.get)
//                .latitudeLocation(response.getLatitude())
//                .longitudeLocation(response.getLongitude())
//                .address(response.getAddress())
//                .description(response.getDescription())
//
//                .build();

        return null;
    }
}
