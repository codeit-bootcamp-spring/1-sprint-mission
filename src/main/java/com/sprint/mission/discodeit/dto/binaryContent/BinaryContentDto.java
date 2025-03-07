package com.sprint.mission.discodeit.dto.binaryContent;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.time.Instant;
import java.util.UUID;

public record BinaryContentDto(
    UUID id,
    String fileName,
    byte[] bytes,
    String contentType,
    Instant createdAt,
    Long size
) {

  public static BinaryContentDto from(BinaryContent binaryContent) {
    return new BinaryContentDto(
        binaryContent.getId(),
        binaryContent.getFilename(),
        binaryContent.getBinaryImage(),
        binaryContent.getContentType(),
        binaryContent.getCreatedAt(),
        binaryContent.getSize()
    );
  }
}
