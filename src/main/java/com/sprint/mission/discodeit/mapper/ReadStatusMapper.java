package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReadStatusMapper {

  public ReadStatusDto toDto(ReadStatus readStatus) {
    return ReadStatusDto.builder()
        .id(readStatus.getId())  // UUID
        .userId(readStatus.getUser().getId())  // UUID
        .channelId(readStatus.getChannel().getId())  // UUID
        .lastReadAt(readStatus.getLastReadAt())  // Instant
        .build();
  }

}
