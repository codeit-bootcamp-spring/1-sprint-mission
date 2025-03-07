package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.binarycontetnt.BinaryContentResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;

public class BinaryContentMapper {

  public static BinaryContentResponse toDto(BinaryContent bc) {
    return new BinaryContentResponse(bc.getId(), bc.getFileName(), bc.getSize(), bc.getMimeType(),
        bc.getBytes());
  }
}
