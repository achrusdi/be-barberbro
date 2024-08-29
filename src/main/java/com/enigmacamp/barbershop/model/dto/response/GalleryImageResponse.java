package com.enigmacamp.barbershop.model.dto.response;

import com.enigmacamp.barbershop.model.entity.Barbers;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GalleryImageResponse {
    private String image_id;

    private String barbers_id;

    private String name;

    private String path;

    private Long size;

    private String contentType;
}