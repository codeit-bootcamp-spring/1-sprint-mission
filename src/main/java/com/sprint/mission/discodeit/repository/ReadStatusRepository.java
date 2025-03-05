package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository {
    List<ReadStatus> findAllByUserId(UUID uuid);

    ReadStatus save(ReadStatus readStatus);

    Optional<ReadStatus> findById(UUID id);

    List<ReadStatus> findAllByChannelId(UUID channelId);

    boolean existsById(UUID id);

    void deleteById(UUID id);

    void deleteAllByChannelId(UUID channelId);
}
