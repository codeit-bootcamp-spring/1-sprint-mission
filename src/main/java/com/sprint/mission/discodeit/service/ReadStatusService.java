package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.ReadStatus;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusService {
    void markAsRead(UUID userId, UUID channelId, Instant timestamp);
    Optional<ReadStatus> getLastRead(UUID userId, UUID channelId);
    List<ReadStatus> getUserReadStatuses(UUID userId);
    List<ReadStatus> getChannelReadStatuses(UUID channelId);
    void deleteByUserId(UUID userId);
    void deleteByChannelId(UUID channelId);
}
