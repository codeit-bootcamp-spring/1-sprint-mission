package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import org.springframework.stereotype.Component;

@Component
public class ReadStatusMapper {

  public ReadStatusDto toReadStatusDto(ReadStatus readStatus) {
    return new ReadStatusDto(readStatus.getId(), readStatus.getCreatedAt(),
        readStatus.getUpdatedAt(),
        readStatus.getUser().getId(), readStatus.getChannel().getId(), readStatus.getLastReadAt());
  }
}
