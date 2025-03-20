package com.sprint.mission.discodeit.mapper.entitymapper;

import com.sprint.mission.discodeit.dto.response.ReadStatusDto;
import com.sprint.mission.discodeit.entity.status.read.ReadStatus;

public class ReadStatusMapper {

  public static ReadStatusDto toDto(ReadStatus readStatus) {
    return new ReadStatusDto(readStatus.getId(), readStatus.getUser().getId(),
        readStatus.getChannel().getId(), readStatus.getLastReadAt());
  }
}
