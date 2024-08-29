package com.enigmacamp.barbershop.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enigmacamp.barbershop.model.dto.request.GalleryImageRequest;
import com.enigmacamp.barbershop.model.dto.response.CommonResponse;
import com.enigmacamp.barbershop.model.dto.response.GalleryImageResponse;
import com.enigmacamp.barbershop.service.GalleryImageService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class GalleryImageController {
    private final GalleryImageService galleryImageService;

    @PostMapping("/gallery-image")
    public ResponseEntity<CommonResponse<List<GalleryImageResponse>>> create(@RequestBody GalleryImageRequest request,
            HttpServletRequest srvrequest) {
        System.out.println(request);
        List<GalleryImageResponse> galleryImages = galleryImageService.saveGalleryImages(request, srvrequest);

        return ResponseEntity.ok(CommonResponse.<List<GalleryImageResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Images uploaded successfully")
                .data(galleryImages)
                .build());

    }
}