package com.sprint.mission.repository.jcf;

import com.sprint.mission.entity.addOn.BinaryContent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BinarycontentRepository {
    BinaryContent save(BinaryContent binaryContent);

    Optional<BinaryContent> findById(UUID id);

    List<BinaryContent> findAllByIdIn(List<UUID> idList);

    void delete(UUID id);

    boolean existsById(UUID id);
}
