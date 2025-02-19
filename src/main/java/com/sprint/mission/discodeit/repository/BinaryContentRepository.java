package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BinaryContentRepository {
    BinaryContent save(BinaryContent content);

    BinaryContent findById(UUID id);

    Optional<BinaryContent> findByUserId(UUID userId);

    List<BinaryContent> findByMessageId(UUID messageId);

    void delete(UUID id);
}
