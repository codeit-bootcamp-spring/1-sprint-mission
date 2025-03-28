package com.sprint.mission.discodeit.dto.read_status;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;

public record ReadStatusDto(
    Channel channel,
    User user,
    Instant lastReadAt
) {

  public static ReadStatusDto fromEntity(ReadStatus readStatus) {
    return new ReadStatusDto(
        readStatus.getChannel(),
        readStatus.getUser(),
        readStatus.getLastReadAt()
    );
  }
}
