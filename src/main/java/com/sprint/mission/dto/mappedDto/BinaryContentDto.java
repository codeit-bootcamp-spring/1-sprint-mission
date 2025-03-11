package com.sprint.mission.dto.mappedDto;

import java.util.UUID;

public record BinaryContentDto(
        UUID id,
        String fileName,
        Long size,
        String contentType,
        byte[] bytes) {
}
