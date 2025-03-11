package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.ReadStatusDto;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;

public class ReadStatusMapper {
  public static ReadStatusDto toDto(ReadStatus readStatus) {
    if(readStatus == null) return null;
    return new ReadStatusDto(readStatus.getId(), readStatus.getUser().getId(), readStatus.getChannel().getId(), readStatus.getLastReadAt());
  }
}
