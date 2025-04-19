package com.sprint.mission.service;

import com.sprint.mission.dto.request.BinaryContentDtoForCreate;
import com.sprint.mission.entity.BinaryContent;
import java.util.List;
import java.util.UUID;

public interface BinaryService {

  BinaryContent create(BinaryContentDtoForCreate request);

  BinaryContent findById(UUID id);

  void deleteById(UUID binaryId);

  List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds);
}
