package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BinaryContentRepository {
    BinaryContent save(BinaryContent binaryContent);
    Optional<BinaryContent> findById(UUID binaryContentId);
    List<BinaryContent> findAllById(List<UUID> binaryContentIds);
    void deleteById(UUID binaryContentId);
    boolean existsById(UUID binaryContentId);
}