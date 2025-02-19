package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository {
    ReadStatus save(ReadStatus readStatus);
    Optional<ReadStatus> findById(UUID id);
    List<ReadStatus> findAllUserId(UUID userId);
    List<ReadStatus> findAllChannelId(UUID channelId);
    void deleteById(UUID id);
    void deleteAllByChannelId(UUID id);
    boolean existsByUserIdAndChannelId(UUID userid, UUID channelId);
}
