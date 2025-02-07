package com.sprint.mission.discodeit.repository.interfacepac;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public interface BinaryContentRepository {
    void save(BinaryContent binaryContent);
    byte[]findByUserId(UUID userId);
    List<BinaryContent> findByMessageId(UUID messageId);
    void deleteByMessageId(UUID messageId);

}
