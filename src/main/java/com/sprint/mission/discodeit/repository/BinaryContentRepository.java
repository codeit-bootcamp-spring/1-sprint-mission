package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BinaryContentRepository {
    BinaryContent save(BinaryContent binaryContent);

    BinaryContent findById(UUID id);

    List<BinaryContent> findAllByIdIn(List<UUID> ids);

    boolean existsById(UUID id);

    void deleteById(UUID id);
}