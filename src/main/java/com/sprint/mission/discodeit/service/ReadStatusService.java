package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.status.CreateReadStatusRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatus create(CreateReadStatusRequest request);

    List<ReadStatus> findAllByUserId(UUID userId);

    void updateReadStatus(UUID userId, UUID channelId);
}
