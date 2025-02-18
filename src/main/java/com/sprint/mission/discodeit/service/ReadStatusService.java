package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ReadStatusDto;
import java.time.Instant;
import java.util.UUID;

public interface ReadStatusService {
  ReadStatusDto createReadStatus(UUID paramUUID1, UUID paramUUID2);
  ReadStatusDto findReadStatusById(UUID paramUUID);
  ReadStatusDto findLastReadMessage(UUID paramUUID1, UUID paramUUID2);
  void updateLastReadAt(UUID paramUUID, Instant paramInstant);
  void deleteReadStatus(UUID paramUUID);
}