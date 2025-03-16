package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import org.springframework.stereotype.Component;

@Component
public class ReadStatusMapper {

  public ReadStatusDto toDto(ReadStatus readStatus) {
    return new ReadStatusDto(
        readStatus.getId(),

        //User fetch join으로 가져오자
        readStatus.getUser().getId(),

        //Channel fetch join으로 가져오자
        readStatus.getChannel().getId(),
        readStatus.getLastReadAt()
    );
  }
}
