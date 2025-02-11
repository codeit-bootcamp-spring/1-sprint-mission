package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;

public interface BinaryContentRepository {
    BinaryContent findById(Long id);
    void save(BinaryContent binaryContent);
}
