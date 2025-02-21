package com.sprint.mission.discodeit.dto.response.binary;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.time.Instant;
import java.util.UUID;

public record BinaryContentResponseData(
    UUID binaryContentId,
    UUID userId,
    String filename,
    String contentType,
    Instant createdAt
) {

  public static BinaryContentResponseData fromEntity(BinaryContent binaryContent) {
    return new BinaryContentResponseData(
        binaryContent.getId(),
        binaryContent.getUserId(),
        binaryContent.getFilename(),
        binaryContent.getContentType(),
        binaryContent.getCreatedAt()
    );
  }
}
