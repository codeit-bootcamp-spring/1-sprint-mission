package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository {
    ReadStatus save(ReadStatus readStatus);
    Optional<ReadStatus> findById(UUID id);
    List<ReadStatus> findAllByUserId(UUID userId);
    List<ReadStatus> findAllByChannelId(UUID channelId);
    Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId); // ✅ 추가됨
    void deleteById(UUID id);
    void deleteByUserId(UUID userId);
    void deleteByChannelId(UUID channelId);
    long count();
}
