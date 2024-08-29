package com.enigmacamp.barbershop.service.Impl;

import com.enigmacamp.barbershop.constant.ResponseMessage;
import com.enigmacamp.barbershop.model.entity.BarberProfilePicture;
import com.enigmacamp.barbershop.repository.BarbersProfilePictureRepository;
import com.enigmacamp.barbershop.service.BarbersProfilePictureService;

import jakarta.annotation.PostConstruct;
import jakarta.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class BarbersProfilePictureServiceImpl implements BarbersProfilePictureService {
    private final Path directoryPath;
    private final BarbersProfilePictureRepository barbersProfilePictureRepository;
    private final String mainPath = "src/main/resources/static";
    private final String secondPath = "/assets/images/barbershop";

    @Autowired
    public BarbersProfilePictureServiceImpl(@Value(mainPath + secondPath) String directoryPath,
            BarbersProfilePictureRepository barbersProfilePictureRepository) {
        this.directoryPath = Paths.get(directoryPath);
        this.barbersProfilePictureRepository = barbersProfilePictureRepository;
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
    public BarberProfilePicture create(MultipartFile multipartFile) {
        try {
            if (!List.of("image/jpeg", "image/png", "image/jpg", "image/svg+xml")
                    .contains(multipartFile.getContentType()))
                throw new ConstraintViolationException(ResponseMessage.ERROR_INVALID_CONTENT_TYPE, null);
            String originalFilename = multipartFile.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));

            String uniqueFilename = UUID.randomUUID().toString() + extension;
            Path filePath = directoryPath.resolve(uniqueFilename);
            Files.copy(multipartFile.getInputStream(), filePath);
            String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

            BarberProfilePicture barberProfilePicture = BarberProfilePicture.builder()
                    .name(uniqueFilename)
                    .contentType(multipartFile.getContentType())
                    .size(multipartFile.getSize())
                    .path(baseUrl + secondPath + "/" + uniqueFilename)
                    .createdAt(System.currentTimeMillis())
                    .updatedAt(System.currentTimeMillis())
                    .build();

            barbersProfilePictureRepository.saveAndFlush(barberProfilePicture);

            return barberProfilePicture;

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public Resource getById(String id) {
        try {
            BarberProfilePicture image = barbersProfilePictureRepository.findById(id).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND));
            Path filePath = Paths.get(image.getPath());
            if (!Files.exists(filePath))
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND);
            return new UrlResource(filePath.toUri());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public void deleteById(String id) {
        try {
            BarberProfilePicture image = barbersProfilePictureRepository.findById(id)
                    .orElseThrow(
                            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND));
            Path filePath = Paths.get(image.getPath());
            if (!Files.exists(filePath))
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND);
            Files.delete(filePath);
            barbersProfilePictureRepository.delete(image);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public BarberProfilePicture getByName(String name) {
        return barbersProfilePictureRepository.getByName(name);
    }
}
