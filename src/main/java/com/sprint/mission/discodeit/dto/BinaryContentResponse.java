package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.BinaryContent;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record BinaryContentResponse(
        UUID id,
        Instant createdAt,
        String fileName,
        String contentType,
        byte[] bytes
) {
    public static BinaryContentResponse entityToDto(BinaryContent binaryContent) {
        return BinaryContentResponse.builder()
                .id(binaryContent.getId())
                .createdAt(binaryContent.getCreatedAt())
                .fileName(binaryContent.getFileName())
                .contentType(binaryContent.getContentType())
                .bytes(binaryContent.getBytes())
                .build();
    }
}
