package com.enigmacamp.barbershop.service.Impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.enigmacamp.barbershop.constant.ResponseMessage;
import com.enigmacamp.barbershop.model.dto.request.GalleryImageRequest;
import com.enigmacamp.barbershop.model.dto.response.GalleryImageResponse;
import com.enigmacamp.barbershop.model.entity.Barbers;
import com.enigmacamp.barbershop.model.entity.GalleryImage;
import com.enigmacamp.barbershop.model.entity.Users;
import com.enigmacamp.barbershop.repository.BarbersRepository;
import com.enigmacamp.barbershop.repository.GalleryImageRepository;
import com.enigmacamp.barbershop.service.BarberService;
import com.enigmacamp.barbershop.service.GalleryImageService;
import com.enigmacamp.barbershop.util.JwtHelpers;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GalleryImageServiceImpl implements GalleryImageService {

    private final Path directoryPath;
    private final GalleryImageRepository galleryImageRepository;
    private final JwtHelpers jwtHelpers;
    private final BarberService barberService;
    private final String mainPath = "src/main/resources/static";
    private final String secondPath = "/assets/images/gallery-images";

    @Autowired
    public GalleryImageServiceImpl(@Value(mainPath + secondPath) String directoryPath,
            GalleryImageRepository galleryImageRepository, JwtHelpers jwtHelpers, BarberService barberService) {
        this.barberService = barberService;
        this.jwtHelpers = jwtHelpers;
        this.directoryPath = Paths.get(directoryPath);
        this.galleryImageRepository = galleryImageRepository;
    }

    @PostConstruct
    public void initDirectory() {
        if (!Files.exists(directoryPath)) {
            try {
                Files.createDirectories(directoryPath);
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }
    }

    @Override
    public List<GalleryImageResponse> saveGalleryImages(GalleryImageRequest images, HttpServletRequest srvrequest) {
        List<GalleryImageResponse> responses = new ArrayList<>();

        for (MultipartFile image : images.getImages()) {
            if (image.isEmpty())
                continue;

            try {
                if (!List.of("image/jpeg", "image/png", "image/jpg", "image/svg+xml")
                        .contains(image.getContentType()))
                    throw new ConstraintViolationException(ResponseMessage.ERROR_INVALID_CONTENT_TYPE, null);

                Users user = jwtHelpers.getUser(srvrequest);
                if (user == null) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
                }

                Barbers barbers = barberService.getByEmail(user.getEmail());

                if (barbers == null) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND);
                }

                String originalFilename = image.getOriginalFilename();
                String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));

                String uniqueFilename = UUID.randomUUID().toString() + extension;
                Path filePath = directoryPath.resolve(uniqueFilename);
                Files.copy(image.getInputStream(), filePath);
                // String baseUrl =
                // ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

                GalleryImage galleryImage = GalleryImage.builder()
                        .name(uniqueFilename)
                        .contentType(image.getContentType())
                        .size(image.getSize())
                        .path(secondPath + "/" + uniqueFilename)
                        .createdAt(System.currentTimeMillis())
                        .updatedAt(System.currentTimeMillis())
                        .barbers_id(barbers)
                        .build();

                galleryImageRepository.saveAndFlush(galleryImage);

                GalleryImageResponse response = GalleryImageResponse.builder()
                        .name(galleryImage.getName())
                        .contentType(galleryImage.getContentType())
                        .size(galleryImage.getSize())
                        .path(galleryImage.getPath())
                        .barbers_id(barbers.getId())
                        .build();

                responses.add(response);

            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Error saving file: " + e.getMessage());
            } catch (ConstraintViolationException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Unexpected error: " + e.getMessage());
            }
        }

        return responses;
    }

    @Override
    public Resource getById(String id) {
        return null;
    }

    @Override
    public void deleteById(String id) {
    }

    @Override
    public GalleryImage getByName(String name) {
        return null;
    }

}