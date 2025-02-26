package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ReadStatusDto;
import java.time.Instant;
import java.util.UUID;

public interface ReadStatusService {
  ReadStatusDto createReadStatus(UUID userId, UUID channelId, Instant lastReadAt);
  ReadStatusDto findReadStatusById(UUID id);
  ReadStatusDto findLastReadMessage(UUID userId, UUID channelId);
  void updateLastReadAt(UUID id, Instant newTime);
  void deleteReadStatus(UUID id);
}
