package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.Map;
import java.util.UUID;

public interface BinaryContentRepository {
    void save(BinaryContent binaryContent);
    BinaryContent findById(UUID id);
    Map<UUID, BinaryContent> findALl();
    void delete(UUID id);
}