package com.sprint.mission.discodeit.dto.request;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.UUID;

public record BinaryContentCreateRequest(
    UUID id,
    String fileName,
    Long size,
    String contentType,
    byte[] bytes
) {

  public BinaryContentCreateRequest(String fileName, Long size, String contentType, byte[] bytes) {
    this(null, fileName, size, contentType, bytes);
  }

  public BinaryContentCreateRequest(BinaryContent binaryContent){
    this(binaryContent.getId(), binaryContent.getFileName(), binaryContent.getSize(), binaryContent.getContentType(), null);
  }

}
