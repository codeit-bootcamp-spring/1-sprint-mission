package com.sprint.mission.discodeit.dto.binary_content;

import com.sprint.mission.discodeit.entity.BinaryContent;

public record BinaryContentDto(
    String fileName,
    String contentType,
    byte[] file,
    Long size
) {

  public static BinaryContentDto fromEntity(BinaryContent binaryContent) {
    return new BinaryContentDto(
        binaryContent.getFileName(),
        binaryContent.getContentType(),
        binaryContent.getFile(),
        binaryContent.getSize()
    );
  }
}
