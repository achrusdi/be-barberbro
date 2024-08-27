package com.enigmacamp.barbershop.controller;

import com.enigmacamp.barbershop.model.dto.response.CommonResponse;
import com.enigmacamp.barbershop.model.entity.BarbersAvatar;
import com.enigmacamp.barbershop.service.BarbersAvatarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BarbersAvatarController {
    private final BarbersAvatarService barbersAvatarService;

    @PostMapping("/upload/avatar")
    public ResponseEntity<CommonResponse<BarbersAvatar>> create (@RequestParam("avatar") MultipartFile image){
        BarbersAvatar barbersAvatar = barbersAvatarService.create(image);
        return ResponseEntity.ok(CommonResponse.<BarbersAvatar>builder()
                .message("Avatar uploaded successfully")
                .data(barbersAvatar)
                .build());
    }

}
