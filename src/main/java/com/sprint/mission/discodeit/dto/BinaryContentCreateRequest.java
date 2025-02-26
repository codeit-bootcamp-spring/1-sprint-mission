package com.sprint.mission.discodeit.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record BinaryContentCreateRequest (
        String fileName,
        Long size,
        String contentType,
        byte[] bytes
){
    public BinaryContentCreateRequest(MultipartFile file) throws Exception{
        this(
                file.getOriginalFilename(),
                file.getSize(),
                file.getContentType(),
                file.getBytes()
        );
    }
}
