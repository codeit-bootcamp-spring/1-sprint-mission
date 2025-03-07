package com.sprint.mission.discodeit.dto.status;

import com.sprint.mission.discodeit.entity.ReadStatus;
import java.time.Instant;
import java.util.UUID;

public record ReadStatusResponse(UUID id, UUID userId, UUID channelId, Instant lastReadAt) {

  public static ReadStatusResponse fromEntity(ReadStatus rs) {
    return new ReadStatusResponse(rs.getId(), rs.getOwner().getId(), rs.getChannel().getId(),
        rs.getLastReadTime());
  }
}
