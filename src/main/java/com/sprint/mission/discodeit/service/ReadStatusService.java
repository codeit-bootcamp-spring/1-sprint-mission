package com.sprint.mission.discodeit.service;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatus create(UUID userId, UUID channelId);
    ReadStatus find(UUID id);
    List<ReadStatus> findAllByUserId(UUID userId);
    List<ReadStatus> findAllByChannelId(UUID channelId);
    ReadStatus update(UUID id);
    void delete(UUID id);
    void deleteAllByChannelId(UUID channelId);
}
