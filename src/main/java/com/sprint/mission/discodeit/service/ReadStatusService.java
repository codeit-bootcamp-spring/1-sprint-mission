package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatus create(UUID userId, UUID channelId, UUID messageId);

    ReadStatus findById(UUID id);

    List<ReadStatus> findAllByUserId(UUID userId);

    ReadStatus update(UUID id, ReadStatusUpdateRequest request);

    void deleteById(UUID id);
}