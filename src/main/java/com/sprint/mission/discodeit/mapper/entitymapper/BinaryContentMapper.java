package com.sprint.mission.discodeit.mapper.entitymapper;

import com.sprint.mission.discodeit.dto.response.BinaryContentDto;
import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;

public class BinaryContentMapper {

  public static BinaryContentDto toDto(BinaryContent binaryContent) {
    if (binaryContent == null) {
      return null;
    }

    return new BinaryContentDto(binaryContent.getId(), binaryContent.getFileName(),
        binaryContent.getSize(),
        binaryContent.getContentType());
  }
}
