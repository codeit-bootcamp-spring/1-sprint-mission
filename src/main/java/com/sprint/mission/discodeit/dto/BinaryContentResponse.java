package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.lang.management.LockInfo;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record BinaryContentResponse(
    UUID id,
    Instant createdAt,
    String fileName,
    Long size,
    String contentType
) {

  public static BinaryContentResponse entityToDto(BinaryContent binaryContent) {
    return BinaryContentResponse.builder()
        .id(binaryContent.getId())
        .createdAt(binaryContent.getCreatedAt())
        .fileName(binaryContent.getFileName())
        .size(binaryContent.getSize())
        .contentType(binaryContent.getContentType())
        .build();
  }
}
