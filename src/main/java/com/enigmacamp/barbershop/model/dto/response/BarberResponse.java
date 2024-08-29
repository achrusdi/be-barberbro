package com.enigmacamp.barbershop.model.dto.response;

import com.enigmacamp.barbershop.model.entity.Barbers;
import com.enigmacamp.barbershop.model.entity.BarberProfilePicture;
import com.enigmacamp.barbershop.model.entity.Users;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BarberResponse {
    private String id;
    private String name;
    private String contact_number;
    private String email;
    private String street_address;
    private String city;
    private String state_province_region;
    private String postal_zip_code;
    private String country;
    private Double latitude;
    private Double longitude;
    private String description;
    // private Users userId;
    private float balance;
    private Boolean verified;
    private BarberProfilePicture barbershop_profile_picture_id;
    private Long createdAt;
    private Long updateAt;

    public Barbers toEntity() {
        return Barbers.builder()
                .id(id)
                .name(name)
                .contact_number(contact_number)
                .email(email)
                .street_address(street_address)
                .city(city)
                .state_province_region(state_province_region)
                .postal_zip_code(postal_zip_code)
                .country(country)
                .latitude(latitude)
                .longitude(longitude)
                .description(description)
                // .userId(userId)
                .balance(balance)
                .verified(verified)
                .createdAt(createdAt)
                .updateAt(updateAt)
                .build();
    }
}
