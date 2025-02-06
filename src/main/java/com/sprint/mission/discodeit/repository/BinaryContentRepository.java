package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public interface BinaryContentRepository {
    BinaryContent save(BinaryContent binaryContent);
    BinaryContent findByUserIdAndMessageId(UUID userId, UUID messageId);
    BinaryContent findByUserId(UUID userId);
    BinaryContent findByContentId(UUID contentId);
    List<BinaryContent> findAllByContentId(List<UUID> ids);
    void deleteByUserId(UUID userId);
    void deleteByContentId(UUID contentId);
}
