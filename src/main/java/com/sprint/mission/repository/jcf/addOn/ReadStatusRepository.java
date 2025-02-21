package com.sprint.mission.repository.jcf.addOn;

import com.sprint.mission.entity.addOn.ReadStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository {
    ReadStatus save(ReadStatus readStatus);

    Optional<ReadStatus> findById(UUID id);

    List<ReadStatus> findAllByUserId(UUID userId);

    List<ReadStatus> findAllByChannelId(UUID channelId);

    boolean existsById(UUID id);

    void deleteById(UUID id);

    void deleteAllByChannelId(UUID channelId);
}
