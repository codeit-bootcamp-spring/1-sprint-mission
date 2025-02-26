package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.Map;
import java.util.UUID;

public interface BinaryContentRepository {
    Map<UUID, BinaryContent> loadBinaryContentText();
    void saveBinaryContentText();
}
