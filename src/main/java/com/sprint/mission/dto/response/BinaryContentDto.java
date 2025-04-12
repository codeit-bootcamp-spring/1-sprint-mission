package com.sprint.mission.dto.response;

import java.util.UUID;

public record BinaryContentDto(
        UUID id,
        String fileName,
        Long size,
        String contentType,
        byte[] bytes) {
}
