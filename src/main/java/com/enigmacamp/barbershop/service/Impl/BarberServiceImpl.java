package com.enigmacamp.barbershop.service.Impl;

import org.springframework.stereotype.Service;

import com.enigmacamp.barbershop.model.dto.request.BarberRequest;
import com.enigmacamp.barbershop.model.dto.response.BarberResponse;
import com.enigmacamp.barbershop.model.entity.Barbers;
import com.enigmacamp.barbershop.repository.BarbersRepository;
import com.enigmacamp.barbershop.service.BarberService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BarberServiceImpl implements BarberService {

    private final BarbersRepository barbersRepository;

    @Override
    public BarberResponse create(BarberRequest request) {
        try {
            Barbers barbers = Barbers.builder()
                    .name(request.getName())
                    .latitudeLocation(request.getLatitude())
                    .longitudeLocation(request.getLongitude())
                    .address(request.getAddress())
                    .description(request.getDescription())
                    // .portfolioImages(request.getPortfolioImages())
                    .build();

            barbersRepository.save(barbers);

            return barbers.toResponse();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
