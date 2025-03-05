package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.time.Instant;
import java.util.UUID;

/*
  private UUID id;
  private Instant createdAt;
  //
  private String fileName;
  private Long size;
  private String contentType;
  private byte[] bytes;

 */
public record BinaryContentResponse(
    UUID id,
    Instant createdAt,
    String fileName,
    Long size,
    String contentType,
    byte[] bytes
) {

  public static BinaryContentResponse from(BinaryContent content) {
    return new BinaryContentResponse(
        content.getId(),
        content.getCreatedAt(),
        content.getFileName(),
        content.getSize(),
        content.getContentType(),
        content.getBytes()
    );
  }
}
