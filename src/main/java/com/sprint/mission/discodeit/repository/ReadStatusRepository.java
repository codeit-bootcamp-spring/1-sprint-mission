package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.Map;
import java.util.UUID;

public interface ReadStatusRepository {
    void save(ReadStatus readStatus);
    ReadStatus findById(UUID id);
    Map<UUID, ReadStatus> findALl();
    void delete(UUID id);
}