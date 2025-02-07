package com.sprint.mission.discodeit.repository.interfacepac;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.UUID;

public interface BinaryContentRepository {
    void save(BinaryContent binaryContent);
    byte[]findByUserId(UUID userId);



}
