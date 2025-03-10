package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {

  BinaryContent create(BinaryContentCreateRequest dto);

  BinaryContent find(UUID id);

  List<BinaryContent> findAll();

  List<BinaryContent> findAllByIdIn(List<UUID> ids);

  UUID delete(UUID id);

}
