package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {

    UUID create(BinaryContentCreateDTO binaryContentCreateDTO);
    BinaryContent find(UUID id);
    List<BinaryContent> findAll();
    List<BinaryContent> findAllByIdIn(List<UUID> ids);
    UUID delete(UUID id);

}
