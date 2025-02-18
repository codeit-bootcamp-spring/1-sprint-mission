package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {

    BinaryContent create(BinaryContentCreateDTO binaryContentCreateDTO);

    BinaryContent findById(UUID uuid);
    List<BinaryContent> findAllByIdIn(List<UUID> uuidList);

    void delete(UUID uuid);
}
