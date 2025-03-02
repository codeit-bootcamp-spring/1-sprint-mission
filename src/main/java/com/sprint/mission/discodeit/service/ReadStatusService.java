package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

  // messageId 파라미터를 lastReadAt으로 변경
  ReadStatus create(UUID userId, UUID channelId, Instant lastReadAt);

  ReadStatus findById(UUID id);

  List<ReadStatus> findAllByUserId(UUID userId);

  ReadStatus update(UUID id, ReadStatusUpdateRequest request);

  void deleteById(UUID id);
}