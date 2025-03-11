package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.BinaryContent;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record BinaryContentDto(
    UUID id,
    String fileName,
    Long size,

    @NotBlank
    String contentType
) {

  public BinaryContentDto(UUID binaryContentId) {
    this(binaryContentId, null, null, null);
  }
  public BinaryContentDto(BinaryContent binaryContent){
    this(binaryContent.getId(), binaryContent.getFileName(), binaryContent.getSize(), binaryContent.getContentType());
  }
}
