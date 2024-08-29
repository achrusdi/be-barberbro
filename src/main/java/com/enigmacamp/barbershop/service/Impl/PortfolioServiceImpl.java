package com.enigmacamp.barbershop.service.Impl;

import com.enigmacamp.barbershop.constant.ResponseMessage;
import com.enigmacamp.barbershop.model.entity.BarberProfilePicture;
import com.enigmacamp.barbershop.model.entity.Portfolio;
import com.enigmacamp.barbershop.repository.BarbersProfilePictureRepository;
import com.enigmacamp.barbershop.repository.PortfolioRepository;
import com.enigmacamp.barbershop.service.PortfolioService;
import com.enigmacamp.barbershop.util.AuthTokenExtractor;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class PortfolioServiceImpl implements PortfolioService {
    private final Path directoryPath;
    private final PortfolioRepository portfolioRepository;
    private final AuthTokenExtractor authTokenExtractor;


    @Autowired
    public PortfolioServiceImpl(@Value("${app.barberShop.portfolio.multipart.path-location}") String directoryPath,
                                PortfolioRepository portfolioRepository,
                                AuthTokenExtractor authTokenExtractor) {
        this.directoryPath = Paths.get(directoryPath);
        this.portfolioRepository = portfolioRepository;
        this.authTokenExtractor = authTokenExtractor;

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
    public Portfolio create(HttpServletRequest request, MultipartFile multipartFile) {
        try {
            if (!List.of("image/jpeg", "image/png", "image/jpg", "image/svg+xml").contains(multipartFile.getContentType()))
                throw new ConstraintViolationException(ResponseMessage.ERROR_INVALID_CONTENT_TYPE, null);
            String uniqueFilename = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
//            uniqueFilename = "public/files/ndog.png";
            Path filePath = directoryPath.resolve(uniqueFilename);
            Files.copy(multipartFile.getInputStream(), filePath);

//            decode token to get id iduser
            authTokenExtractor.getTokenFromHeader(request);

//            query for get barberid by userid

            Portfolio barberProfilePicture = Portfolio.builder()
                    .name(uniqueFilename)
//                    .barberId()
                    .contentType(multipartFile.getContentType())
                    .size(multipartFile.getSize())
                    .path(filePath.toString())
                    .createdAt(System.currentTimeMillis())
                    .updatedAt(System.currentTimeMillis())
                    .build();

            portfolioRepository.saveAndFlush(barberProfilePicture);


            return barberProfilePicture;

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public Resource getById(String id) {
        return null;
    }

    @Override
    public void deleteById(String id) {

    }
}
