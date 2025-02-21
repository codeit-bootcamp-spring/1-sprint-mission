package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.dto.ReadStatusDTO;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.Optional;

public interface ReadStatusRepository {
    ReadStatus save(ReadStatus readStatus);
    Optional<ReadStatus> findByUserIdAndChannelId(String userId, String channelId);
    Optional<ReadStatus> findByUserId(String userId);
    void deleteByUserIdAndChannelId(String userId, String channelId);
}