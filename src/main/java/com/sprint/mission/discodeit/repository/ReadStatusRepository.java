package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.*;
import java.util.UUID;

public interface ReadStatusRepository {
    ReadStatus save(ReadStatus readStatus);

    Optional<ReadStatus> findById(UUID Id);

    Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId);

    List<ReadStatus> findAllByUserId(UUID userId);

    void deleteByUserIdAndChannelId(UUID userId, UUID channelId);

    void deleteByChannelId(UUID channelId);

    List<UUID> findUserIdsByChannelId(UUID channelId);

    boolean existsByUserIdAndChannelId(UUID userId, UUID channelId);

    void deleteByMessageId(UUID messageId);

    void deleteById(UUID id);
}
