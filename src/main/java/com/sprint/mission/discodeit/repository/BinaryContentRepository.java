package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface BinaryContentRepository {
    void save(BinaryContent binaryContent);
    Optional<BinaryContent> findById(UUID id);
    Map<UUID, BinaryContent> findAll();
    void delete(UUID id);
    boolean existsById(UUID id);
}