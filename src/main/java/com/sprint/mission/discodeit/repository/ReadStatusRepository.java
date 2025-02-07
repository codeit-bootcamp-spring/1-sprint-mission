package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository {
    ReadStatus save(ReadStatus readStatus);
    Optional<ReadStatus> findById(UUID id);
    void deleteById(UUID id);
}
