package com.spirnt.mission.discodeit.dto.binaryContent;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record BinaryContentCreate(
        UUID userId,
        UUID messageId,
        MultipartFile file
) {
}
