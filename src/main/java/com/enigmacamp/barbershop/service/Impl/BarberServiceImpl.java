package com.enigmacamp.barbershop.service.Impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BarberServiceImpl implements BarberService {

    private final BarbersRepository barbersRepository;
    private final JwtHelpers jwtHelpers;
    private final EntityManager entityManager;

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
    public Barbers update(Barbers barbers) {
        return barbersRepository.save(barbers);
    }

    @Override
    public List<BarberResponse> getAll() {
        String sql = "SELECT " +
                "b.id, " +
                "b.name, " +
                "b.contact_number, " +
                "b.email, " +
                "b.street_address, " +
                "b.city, " +
                "b.state_province_region, " +
                "b.postal_zip_code, " +
                "b.country, " +
                "b.latitude, " +
                "b.longitude, " +
                "b.description, " +
                "b.balance, " +
                "b.verified, " +
                "b.created_at, " +
                "b.updated_at, " +
                "AVG(r.rating) AS average_rating, " +
                "COUNT(r.id) AS review_count " +
                "FROM m_barbers b " +
                "LEFT JOIN m_bookings bo ON b.id = bo.barber_id " +
                "LEFT JOIN m_reviews r ON bo.booking_id = r.booking_id " +
                "GROUP BY b.id, b.name, b.contact_number, b.email, b.street_address, " +
                "b.city, b.state_province_region, b.postal_zip_code, b.country, b.latitude, " +
                "b.longitude, b.description, b.balance, b.verified, b.created_at, b.updated_at " +
                "ORDER BY average_rating DESC NULLS LAST;";

        Query query = entityManager.createNativeQuery(sql);

        @SuppressWarnings("unchecked")
        List<Object[]> results = query.getResultList();

        return results.stream().map(result -> BarberResponse.builder()
                .id((String) result[0])
                .name((String) result[1])
                .contact_number((String) result[2])
                .email((String) result[3])
                .street_address((String) result[4])
                .city((String) result[5])
                .state_province_region((String) result[6])
                .postal_zip_code((String) result[7])
                .country((String) result[8])
                .latitude((Double) result[9])
                .longitude((Double) result[10])
                .description((String) result[11])
                .balance((float) result[12])
                .verified((Boolean) result[13])
                .createdAt((Long) result[14])
                .updateAt((Long) result[15])
                .averageRating(result[16] == null ? 0 : ((BigDecimal) result[16]).doubleValue())
                .reviewCount((Long) result[17])
                .build()).toList();
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

    @Override
    public List<Barbers> getByNearBy(double latitude, double longitude) {
        try {
            Double distance = 5.0;

            String sql = "SELECT id, name, street_address, city, state_province_region, country, latitude, longitude, balance, description, email, contact_number, created_at, postal_zip_code, verified, updated_at, distance_km "
                    +
                    "FROM ( " +
                    "    SELECT id, name, street_address, city, state_province_region, country, latitude, longitude, balance, description, email, contact_number, created_at, postal_zip_code, verified, updated_at, "
                    +
                    "           (6371 * acos(cos(radians(:latitude)) * cos(radians(latitude)) * cos(radians(longitude) - radians(:longitude)) + "
                    +
                    "           sin(radians(:latitude)) * sin(radians(latitude))) " +
                    "           ) AS distance_km " +
                    "    FROM m_barbers " +
                    ") AS calculated_distances " +
                    "WHERE distance_km <= :distance " +
                    "ORDER BY distance_km ASC";

            Query query = entityManager.createNativeQuery(sql);
            query.setParameter("latitude", latitude);
            query.setParameter("longitude", longitude);
            query.setParameter("distance", distance);

            // return query.getResultList();
            List<Object[]> results = query.getResultList();

            List<Barbers> barbersList = new ArrayList<>();
            for (Object[] result : results) {
                Barbers barber = new Barbers();
                // barber.setId(((Number) result[0]).longValue());
                barber.setId((String) result[0]);
                barber.setName((String) result[1]);
                // barber.setStreetAddress((String) result[2]);
                barber.setStreet_address((String) result[2]);
                barber.setCity((String) result[3]);
                // barber.setStateProvinceRegion((String) result[4]);
                barber.setState_province_region((String) result[4]);
                barber.setCountry((String) result[5]);
                barber.setLatitude((Double) result[6]);
                barber.setLongitude((Double) result[7]);
                barber.setBalance(((Number) result[8]).floatValue());
                barber.setDescription((String) result[9]);
                barber.setEmail((String) result[10]);
                barber.setContact_number((String) result[11]);
                barber.setCreatedAt(((Number) result[12]).longValue());
                barber.setPostal_zip_code((String) result[13]);
                barber.setVerified(((Boolean) result[14]));
                barber.setUpdateAt(((Number) result[15]).longValue());

                barbersList.add(barber);
            }

            return barbersList;

            // return barbersRepository.findNearbyBarbers(radius, radius, radius);
        } catch (Exception e) {
            // TODO: handle exception
            throw new RuntimeException(e);
        }
    }

    @Override
    public Barbers getCurrentBarber(Users user) {
        try {
            return barbersRepository.findByUserId(user).orElse(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
