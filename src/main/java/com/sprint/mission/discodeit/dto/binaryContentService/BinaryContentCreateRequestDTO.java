package com.sprint.mission.discodeit.dto.binaryContentService;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.UUID;

public record BinaryContentCreateRequestDTO(
        String fileName,
        String contentType,
        byte[] bytes
) {
}
