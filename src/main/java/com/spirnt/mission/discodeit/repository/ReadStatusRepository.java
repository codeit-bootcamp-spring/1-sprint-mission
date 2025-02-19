package com.spirnt.mission.discodeit.repository;

import com.spirnt.mission.discodeit.enity.ReadStatus;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository {
    void save(ReadStatus readStatus);

    void delete(UUID id);

    Optional<ReadStatus> findById(UUID id);

    Map<UUID, ReadStatus> findAll();

    // 존재 검증
    boolean existsById(UUID id);
}
