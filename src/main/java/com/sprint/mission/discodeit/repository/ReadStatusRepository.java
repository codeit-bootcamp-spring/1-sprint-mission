package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusRepository {
    ReadStatus save(ReadStatus readStatus);
    ReadStatus findByChannelId(UUID channelId);
    ReadStatus findByUserId(UUID userId);
    ReadStatus findByUserIdAndChannelId(UUID userId, UUID channelId);
    List<ReadStatus> findAllByUserId(UUID userId);
    ReadStatus findById(UUID id);
    void delete(UUID readStatusId);
    void deleteByChannelId(UUID channelId);
    boolean existsByUserIdAndChannelId(UUID userId, UUID channelId);
}

