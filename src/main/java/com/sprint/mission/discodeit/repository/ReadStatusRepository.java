package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository {
    void save(ReadStatus readStatus);
    List<UUID> findUserIdsByChannelId(UUID channelId);
    void deleteByChannelId(UUID channelId);

    Optional<ReadStatus> findById(UUID id);
    List<ReadStatus> findByUserId(UUID userId);
    boolean existsByUserAndChannel(User user, Channel channel);
    void deleteById(UUID id);
    boolean existsById(UUID id);
}
