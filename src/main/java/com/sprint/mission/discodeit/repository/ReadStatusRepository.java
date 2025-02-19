package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository {
    void save(ReadStatus readStatus);
    Optional<ReadStatus> findById(UUID id);
    Map<UUID, ReadStatus> findAll();
    void delete(UUID id);
}