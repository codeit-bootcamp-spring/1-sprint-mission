package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BinaryContentRepository {
    BinaryContent save(BinaryContent binaryContent);

    Optional<BinaryContent> findById(UUID contentId);

    List<BinaryContent> findAllByIdIn(List<UUID> contentIds);

    boolean existsById(UUID contentId);

    void deleteById(UUID contentId);
}
