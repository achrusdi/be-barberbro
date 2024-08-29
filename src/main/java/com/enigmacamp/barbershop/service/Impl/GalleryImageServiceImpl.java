package com.enigmacamp.barbershop.service.Impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.enigmacamp.barbershop.constant.ResponseMessage;
import com.enigmacamp.barbershop.model.entity.BarberProfilePicture;
import com.enigmacamp.barbershop.model.entity.GalleryImage;
import com.enigmacamp.barbershop.repository.BarbersProfilePictureRepository;
import com.enigmacamp.barbershop.repository.GalleryImageRepository;
import com.enigmacamp.barbershop.repository.PortfolioRepository;
import com.enigmacamp.barbershop.service.GalleryImageService;
import com.enigmacamp.barbershop.util.AuthTokenExtractor;

import jakarta.annotation.PostConstruct;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GalleryImageServiceImpl implements GalleryImageService {

    private final Path directoryPath;
    private final GalleryImageRepository galleryImageRepository;
    private final String mainPath = "src/main/resources/static";
    private final String secondPath = "/assets/images/gallery-images";

    @Autowired
    public GalleryImageServiceImpl(@Value(mainPath + secondPath) String directoryPath,
            GalleryImageRepository galleryImageRepository) {
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

    public GalleryImage create(GalleryImage galleryImage) {
        return null;
        // try {
        //     if (!List.of("image/jpeg", "image/png", "image/jpg", "image/svg+xml")
        //             .contains(multipartFile.getContentType()))
        //         throw new ConstraintViolationException(ResponseMessage.ERROR_INVALID_CONTENT_TYPE, null);
        //     String originalFilename = multipartFile.getOriginalFilename();
        //     String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));

        //     String uniqueFilename = UUID.randomUUID().toString() + extension;
        //     Path filePath = directoryPath.resolve(uniqueFilename);
        //     Files.copy(multipartFile.getInputStream(), filePath);
        //     String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

        //     BarberProfilePicture barberProfilePicture = BarberProfilePicture.builder()
        //             .name(uniqueFilename)
        //             .contentType(multipartFile.getContentType())
        //             .size(multipartFile.getSize())
        //             .path(baseUrl + secondPath + "/" + uniqueFilename)
        //             .createdAt(System.currentTimeMillis())
        //             .updatedAt(System.currentTimeMillis())
        //             .build();

        //     barbersProfilePictureRepository.saveAndFlush(barberProfilePicture);

        //     return barberProfilePicture;

        // } catch (IOException e) {
        //     throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        // }
    }
}