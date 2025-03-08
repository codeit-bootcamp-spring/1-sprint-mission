package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.ReadStatusDto;
import com.sprint.mission.discodeit.entity.ReadStatus;

public class ReadStatusMapper {

  public ReadStatusDto toDto(ReadStatus entity) {
    if (entity == null) {
      return null;
    }
    return new ReadStatusDto(
        entity.getId(),
        entity.getCreatedAt(),
        entity.getUpdatedAt(),
        entity.getUser() != null
            ? entity.getUser().getId()
            : null,
        entity.getChannel() != null
            ? entity.getChannel().getId()
            : null,
        entity.getLastReadAt()
    );
  }

}
