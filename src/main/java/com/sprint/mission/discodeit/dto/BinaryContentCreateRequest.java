package com.sprint.mission.discodeit.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record BinaryContentCreateRequest(
    String fileName,
    Long size,
    String contentType,
    byte[] bytes
) {

}
