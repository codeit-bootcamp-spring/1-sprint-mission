package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.UUID;

public interface ReadStatusService {
    ReadStatus create(UUID channelId, UUID userId);
}
