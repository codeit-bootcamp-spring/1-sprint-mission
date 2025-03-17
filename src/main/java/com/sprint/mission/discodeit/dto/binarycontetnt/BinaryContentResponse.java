package com.sprint.mission.discodeit.dto.binarycontetnt;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.UUID;

public record BinaryContentResponse(UUID id, String fileName, Long size, String contentType) {

  public static BinaryContentResponse fromEntity(BinaryContent bc) {
    return new BinaryContentResponse(bc.getId(), bc.getFileName(), bc.getSize(), bc.getMimeType());
  }
}
