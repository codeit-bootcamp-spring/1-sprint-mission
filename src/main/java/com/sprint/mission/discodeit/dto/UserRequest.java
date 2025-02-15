package com.sprint.mission.discodeit.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record UserRequest(
        String name,
        String email,
        String password,
        MultipartFile file
) {

    public record Login(
            String name,
            String password
    ){
    }
}
