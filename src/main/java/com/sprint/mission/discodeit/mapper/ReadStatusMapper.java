package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.ReadStatusResponse;
import com.sprint.mission.discodeit.entity.ReadStatus;
import org.springframework.stereotype.Component;

@Component
public class ReadStatusMapper {

  public ReadStatusResponse entityToDto(ReadStatus readStatus) {
    return ReadStatusResponse.builder()
        .id(readStatus.getId())
        .userId(readStatus.getUser().getId())
        .channelId(readStatus.getChannel().getId())
        .lastReadAt(readStatus.getLastReadAt())
        .build();
  }
}
