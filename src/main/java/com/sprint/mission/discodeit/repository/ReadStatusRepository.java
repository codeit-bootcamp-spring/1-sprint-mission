package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public interface ReadStatusRepository {
    Long save(ReadStatus readStatus);

    ReadStatus load(Long id);

    Map.Entry<Long, ReadStatus> load(UUID uuid);

    Map<Long, ReadStatus> loadAll();

    void delete(Long id);

    void update(Long id, ReadStatus readStatus);

    // 조건 검색
    Map<Long, ReadStatus> findAllByUserId(UUID userId);

    Instant findUpToDateReadTimeByChannelId(UUID uuid);

    void deleteAllByChannelId(UUID channelId);
}
