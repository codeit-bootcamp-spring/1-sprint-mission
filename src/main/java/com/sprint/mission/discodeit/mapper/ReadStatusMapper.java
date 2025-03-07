package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.status.ReadStatusResponse;
import com.sprint.mission.discodeit.entity.ReadStatus;

public class ReadStatusMapper {

  public static ReadStatusResponse toDto(ReadStatus rs) {
    return new ReadStatusResponse(
        rs.getId(),
        rs.getOwner().getId(),
        rs.getChannel().getId(),
        rs.getLastReadTime());
  }
}
