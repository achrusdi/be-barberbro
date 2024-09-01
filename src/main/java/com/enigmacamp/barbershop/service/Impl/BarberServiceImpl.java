package com.enigmacamp.barbershop.service.Impl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.enigmacamp.barbershop.model.dto.request.BarberRequest;
import com.enigmacamp.barbershop.model.dto.response.BarberResponse;
import com.enigmacamp.barbershop.model.entity.Barbers;
import com.enigmacamp.barbershop.model.entity.Users;
import com.enigmacamp.barbershop.repository.BarbersRepository;
import com.enigmacamp.barbershop.service.BarberService;
import com.enigmacamp.barbershop.util.JwtHelpers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BarberServiceImpl implements BarberService {

    private final BarbersRepository barbersRepository;
    private final JwtHelpers jwtHelpers;

    @Override
    public BarberResponse create(HttpServletRequest srvrequest, BarberRequest request) {
        try {

            Users user = jwtHelpers.getUser(srvrequest);
            if (user == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }

            Barbers barbers = Barbers.builder()
                    .name(request.getName())
                    .contact_number(request.getContact_number())
                    .email(request.getEmail())
                    .street_address(request.getStreet_address())
                    .state_province_region(request.getState_province_region())
                    .postal_zip_code(request.getPostal_zip_code())
                    .country(request.getCountry())
                    .latitude(request.getLatitude())
                    .longitude(request.getLongitude())
                    .description(request.getDescription())
                    .userId(user)
                    .balance(0)
                    .verified(false)
                    .createdAt(System.currentTimeMillis())
                    .updateAt(System.currentTimeMillis())
                    .city(request.getCity())
                    .barbershop_profile_picture_id(request.getBarbershop_profile_picture_id())
                    .build();

            barbers = barbersRepository.save(barbers);

            return barbers.toResponse();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Barbers getByEmail(String email) {
        return barbersRepository.findByEmail(email);
    }

    @Override
    public Barbers update(HttpServletRequest srvrequest, BarberRequest request) {
        try {
            Users user = jwtHelpers.getUser(srvrequest);
            if (user == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }
            Barbers barbers = barbersRepository.getById(request.getId());
            if (barbers == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Barber not found");
            }
            barbers.setName(request.getName());
            barbers.setContact_number(request.getContact_number());
            barbers.setEmail(request.getEmail());
            barbers.setStreet_address(request.getStreet_address());
            barbers.setState_province_region(request.getState_province_region());
            barbers.setPostal_zip_code(request.getPostal_zip_code());
            barbers.setCountry(request.getCountry());
            barbers.setLatitude(request.getLatitude());
            barbers.setLongitude(request.getLongitude());
            barbers.setDescription(request.getDescription());
            barbers.setUserId(user);
            barbers.setUpdateAt(System.currentTimeMillis());
            barbers.setCity(request.getCity());
            barbers.setBarbershop_profile_picture_id(request.getBarbershop_profile_picture_id());
            return barbersRepository.save(barbers);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Barbers> getAll() {
        return barbersRepository.findAll();
    }

    @Override
    public Barbers getById(String id) {
        return barbersRepository.findById(id).orElse(null);
    }

    @Override
    public Barbers getByUserId(Users user) {
        try {
            return barbersRepository.findByUserId(user).orElse(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
