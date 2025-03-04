package com.spirnt.mission.discodeit.repository;

import com.spirnt.mission.discodeit.enity.BinaryContent;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface BinaryContentRepository {
    void save(BinaryContent binaryContent);

    void delete(UUID id);

    Optional<BinaryContent> findById(UUID id);

    Map<UUID, BinaryContent> findAll();

    // 존재 검증
    boolean existsById(UUID id);
}
