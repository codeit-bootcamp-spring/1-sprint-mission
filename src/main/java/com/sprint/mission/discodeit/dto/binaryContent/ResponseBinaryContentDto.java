package com.sprint.mission.discodeit.dto.binaryContent;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.time.Instant;

public record ResponseBinaryContentDto(
    String id,
    String fileName,
    byte[] bytes,
    String contentType,
    Instant createdAt,
    long size
) {

  public static ResponseBinaryContentDto from(BinaryContent binaryContent) {
    return new ResponseBinaryContentDto(
        binaryContent.getId(),
        binaryContent.getFilename(),
        binaryContent.getBinaryImage(),
        binaryContent.getContentType(),
        binaryContent.getCreatedAt(),
        binaryContent.getSize()
    );
  }
}
