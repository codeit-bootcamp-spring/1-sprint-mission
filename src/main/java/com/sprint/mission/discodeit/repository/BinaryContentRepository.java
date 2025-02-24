package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.Optional;

public interface BinaryContentRepository {
    BinaryContent save(BinaryContent binaryContent);
    void deleteById(String userId);
    Optional<BinaryContent> findByUserId(String userId);
    List<BinaryContent> findAll();
}