package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Dto.ReadStatusDto;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.HashMap;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatus createReadStatus(UUID userId, UUID channelId);
    ReadStatusDto findReadStatusByUserId(UUID channelId, UUID userId);
    HashMap<UUID, ReadStatusDto> findAllReadStatusByUserId(UUID userId);
    boolean updateReadStatus(UUID channelId, UUID userId);
    boolean deleteReadStatus(UUID channelId, UUID userId);
}
