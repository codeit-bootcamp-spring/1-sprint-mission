package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BinaryContentRepository {
    BinaryContent save(BinaryContent binaryContent);

    Optional<BinaryContent> findById(UUID id);

    List<BinaryContent> findAllByIdIn(List<UUID> ids);

    boolean existsById(UUID id);

    void deleteById(UUID id);

    Optional<BinaryContent> getBinaryContentById(UUID id);

    List<BinaryContent> getBinaryContentListByIds(List<UUID> ids);
}
