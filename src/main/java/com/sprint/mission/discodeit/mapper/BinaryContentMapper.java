package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;

public class BinaryContentMapper {

  public static BinaryContentDto toDto(BinaryContent binaryContent) {
    if (binaryContent == null) {
      return null;
    }
    return new BinaryContentDto(binaryContent.getId(), binaryContent.getFileName(), binaryContent.getSize(), binaryContent.getContentType());
  }
}
