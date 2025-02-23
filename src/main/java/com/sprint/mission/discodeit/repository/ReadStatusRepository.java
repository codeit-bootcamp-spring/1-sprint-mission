package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.domain.entity.ReadStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository {
    ReadStatus createReadStatus(ReadStatus readStatus);
    Optional<ReadStatus> findById(UUID id);
    List<ReadStatus> findAllByUserId(UUID userId);
    List<ReadStatus> findAllByChannelId(UUID channelId);
    List<ReadStatus> findAll();
    void deleteById(UUID id);
}
