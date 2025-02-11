package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.List;
import java.util.Optional;

public interface BinaryContentRepository {
    BinaryContent save(BinaryContent content);
    void deleteById(String id);
    Optional<BinaryContent> findById(String id);
    List<BinaryContent> findAll();
}