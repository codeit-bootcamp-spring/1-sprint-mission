package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.UUID;

public interface BinaryContentRepository {
    BinaryContent findById(UUID id);
    BinaryContent save(BinaryContent binaryContent);
    boolean delete(UUID id);
}
