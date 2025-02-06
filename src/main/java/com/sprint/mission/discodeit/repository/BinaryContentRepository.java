package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BinaryContentRepository {
    void save(BinaryContent newProfile);

    void deleteByUserId(UUID userId);

    Optional<BinaryContent> findById(UUID id);

    List<BinaryContent> findAllByIdIn(List<UUID> ids);

    void deleteById(UUID id);
}
