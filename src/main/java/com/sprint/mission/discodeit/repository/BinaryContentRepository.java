package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;
import com.sprint.mission.discodeit.entity.binarycontent.BinaryContentType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BinaryContentRepository {
    BinaryContent save(BinaryContent binaryContent);

    Optional<BinaryContent> findById(UUID id);

    Optional<BinaryContent> findByUserIdAndType(UUID userId, BinaryContentType binaryContentType);

    List<BinaryContent> findAllContentsByType(UUID typeId);

    void delete(UUID binaryContentId);
}
